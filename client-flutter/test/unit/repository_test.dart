import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:teamfifai_client/core/config/app_config.dart';
import 'package:teamfifai_client/core/errors/app_error.dart';
import 'package:teamfifai_client/core/network/api_service.dart';
import 'package:teamfifai_client/core/session/session_controller.dart';
import 'package:teamfifai_client/data/models/library_models.dart';
import 'package:teamfifai_client/data/repositories/mock/mock_library_repository.dart';
import 'package:teamfifai_client/data/repositories/remote/live_library_repository.dart';

Json fixture(String name) =>
    jsonDecode(File('assets/fixtures/$name.json').readAsStringSync()) as Json;
void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  final base = Uri.parse('https://library.example/api/v1/');
  test(
    'Build config rejects invalid mode, missing URL and repeated prefix risk',
    () {
      expect(
        () => AppConfig(mode: 'demo', apiBaseUrl: ''),
        throwsArgumentError,
      );
      expect(
        () => AppConfig(mode: 'live', apiBaseUrl: ''),
        throwsArgumentError,
      );
      expect(
        () => AppConfig(mode: 'live', apiBaseUrl: 'https://library.example/'),
        throwsArgumentError,
      );
      expect(AppConfig(mode: 'live', apiBaseUrl: '$base').isMock, isFalse);
    },
  );
  test(
    'Models decode nullable fields and UTC timestamps from shared fixtures',
    () {
      final books = PageResult.fromJson(
        fixture('books-before-loan'),
        Book.fromJson,
      );
      expect(books.data.single.anhBia, isNull);
      expect(books.data.single.soBanRanh, 1);
      final loans = PageResult.fromJson(
        fixture('my-loans-after-loan'),
        Loan.fromJson,
      );
      expect(loans.data.single.hanTra, DateTime.utc(2026, 11, 29, 3));
      expect(
        PageResult.fromJson(fixture('my-loans-empty'), Loan.fromJson).data,
        isEmpty,
      );
    },
  );
  test(
    'Live sends exact endpoint, credentials, bearer and encoded search/page',
    () async {
      final requests = <http.Request>[];
      final client = MockClient((request) async {
        requests.add(request);
        final name = switch (request.url.path) {
          '/api/v1/auth/dang-nhap' => 'login-ban_doc',
          '/api/v1/sach' => 'books-before-loan',
          '/api/v1/ban-doc/toi/dang-muon' => 'my-loans-after-loan',
          _ => throw StateError('Unexpected URL: ${request.url}'),
        };
        return http.Response.bytes(utf8.encode(jsonEncode(fixture(name))), 200);
      });
      final repo = LiveLibraryRepository(ApiService(base, client));
      await repo.login('reader@example.test', 'password');
      final books = await repo.books(
        'TOKEN',
        keyword: 'Tô Hoài & sách',
        page: 2,
      );
      await repo.myLoans('TOKEN', page: 3);
      expect(requests.first.method, 'POST');
      expect(jsonDecode(requests.first.body), {
        'email': 'reader@example.test',
        'matKhau': 'password',
      });
      expect(requests[1].headers['Authorization'], 'Bearer TOKEN');
      expect(requests[1].url.queryParameters['tuKhoa'], 'Tô Hoài & sách');
      expect(requests[1].url.queryParameters['page'], '2');
      expect(requests.last.url.queryParameters, {
        'page': '3',
        'pageSize': '20',
      });
      expect(books.data.single.nhanDe, 'Dế Mèn phiêu lưu ký');
    },
  );
  for (final status in [401, 403, 501, 500]) {
    test(
      'HTTP $status is surfaced even for non-JSON proxy response, no fake fallback',
      () async {
        final repo = LiveLibraryRepository(
          ApiService(
            base,
            MockClient(
              (_) async => http.Response('<html>Error</html>', status),
            ),
          ),
        );
        await expectLater(
          repo.myLoans('token'),
          throwsA(isA<AppError>().having((e) => e.status, 'status', status)),
        );
      },
    );
  }
  test('501 contract response has integration message', () async {
    final repo = LiveLibraryRepository(
      ApiService(
        base,
        MockClient(
          (_) async => http.Response(
            jsonEncode(fixture('error-501-chuc_nang_chua_tich_hop')),
            501,
            headers: {'content-type': 'application/json; charset=utf-8'},
          ),
        ),
      ),
    );
    await expectLater(
      repo.myLoans('token'),
      throwsA(
        isA<AppError>().having(
          (e) => e.message,
          'message',
          'Chức năng chưa tích hợp',
        ),
      ),
    );
  });
  test('Malformed successful payload is a controlled app error', () async {
    final repo = LiveLibraryRepository(
      ApiService(base, MockClient((_) async => http.Response('{}', 200))),
    );
    await expectLater(repo.books('token'), throwsA(isA<AppError>()));
  });
  test('Network and timeout are actionable errors', () async {
    final network = ApiService(
      base,
      MockClient((_) async => throw http.ClientException('offline')),
    );
    await expectLater(
      network.request('sach'),
      throwsA(isA<AppError>().having((e) => e.code, 'code', 'NETWORK')),
    );
    final timeout = ApiService(
      base,
      MockClient((_) => Completer<http.Response>().future),
      timeout: const Duration(milliseconds: 1),
    );
    await expectLater(
      timeout.request('sach'),
      throwsA(isA<AppError>().having((e) => e.code, 'code', 'TIMEOUT')),
    );
  });
  test('Mock rejects wrong password, staff role and foreign token', () async {
    final repo = MockLibraryRepository();
    await expectLater(
      repo.login('bandoc@thuviendemo.test', 'wrong'),
      throwsA(
        isA<AppError>().having(
          (e) => e.code,
          'code',
          'SAI_THONG_TIN_DANG_NHAP',
        ),
      ),
    );
    await expectLater(
      repo.login('admin@thuviendemo.test', 'DemoLib!4211'),
      throwsA(isA<AppError>().having((e) => e.status, 'status', 403)),
    );
    await expectLater(
      repo.myLoans('ANOTHER_USER'),
      throwsA(isA<AppError>().having((e) => e.status, 'status', 401)),
    );
  });
  test(
    'Session persists, restores and removes personal data on logout',
    () async {
      SharedPreferences.setMockInitialValues({});
      final prefs = await SharedPreferences.getInstance();
      final config = AppConfig(mode: 'mock', apiBaseUrl: '');
      final repo = MockLibraryRepository();
      final session = SessionController(prefs, config, repo);
      await session.login('bandoc@thuviendemo.test', 'DemoLib!4211');
      final restored = SessionController(prefs, config, repo);
      await restored.restore();
      expect(restored.current!.nguoiDung.id, session.current!.nguoiDung.id);
      final live = SessionController(
        prefs,
        AppConfig(mode: 'live', apiBaseUrl: '$base'),
        repo,
      );
      await live.restore();
      expect(live.current, isNull);
      await restored.logout();
      expect(prefs.getString(config.sessionKey), isNull);
      expect(restored.current, isNull);
    },
  );
  test('Expired live JWT is discarded on restart', () async {
    final config = AppConfig(mode: 'live', apiBaseUrl: '$base');
    final token =
        'header.${base64Url.encode(utf8.encode(jsonEncode({'exp': 1})))}.signature';
    final data = fixture('login-ban_doc')['data'] as Json;
    data['token'] = token;
    SharedPreferences.setMockInitialValues({
      config.sessionKey: jsonEncode(data),
    });
    final prefs = await SharedPreferences.getInstance();
    final session = SessionController(prefs, config, MockLibraryRepository());
    await session.restore();
    expect(session.current, isNull);
    expect(prefs.getString(config.sessionKey), isNull);
    expect(session.notice, contains('hết hạn'));
  });
}
