import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:teamfifai_client/core/config/app_config.dart';
import 'package:teamfifai_client/core/errors/app_error.dart';
import 'package:teamfifai_client/core/session/session_controller.dart';
import 'package:teamfifai_client/data/models/library_models.dart';
import 'package:teamfifai_client/data/repositories/mock/mock_library_repository.dart';
import 'package:teamfifai_client/main.dart';
import 'package:teamfifai_client/shared/widgets/paged_list.dart';

class FixtureBundle extends Fake implements AssetBundle {
  @override
  Future<String> loadString(String key, {bool cache = true}) async =>
      File(key).readAsStringSync();
}

class FailedLoansRepository extends MockLibraryRepository {
  FailedLoansRepository() : super(assets: FixtureBundle());
  AppError? failure;
  bool empty = false;
  @override
  Future<PageResult<Loan>> myLoans(String token, {int page = 1}) async {
    if (failure != null) throw failure!;
    if (empty) return const PageResult([], page: 1, pageSize: 20, total: 0);
    return super.myLoans(token, page: page);
  }
}

void main() {
  Future<SessionController> sessionFor(MockLibraryRepository repo) async {
    SharedPreferences.setMockInitialValues({});
    final session = SessionController(
      await SharedPreferences.getInstance(),
      AppConfig(mode: 'mock', apiBaseUrl: ''),
      repo,
    );
    await session.login('bandoc@thuviendemo.test', 'DemoLib!4211');
    return session;
  }

  for (final status in [403, 501]) {
    testWidgets(
      'Loans $status displays error without fixtures and retries successfully',
      (tester) async {
        tester.view.physicalSize = const Size(390, 844);
        tester.view.devicePixelRatio = 1;
        addTearDown(tester.view.resetPhysicalSize);
        addTearDown(tester.view.resetDevicePixelRatio);
        final repo = FailedLoansRepository()
          ..failure = AppError(
            'TEST',
            status == 501 ? 'Chức năng chưa tích hợp' : 'Bạn không có quyền.',
            status: status,
          );
        final session = await sessionFor(repo);
        await tester.pumpWidget(LibraryApp(session: session));
        await tester.pumpAndSettle();
        await tester.tap(find.text('Đang mượn'));
        await tester.pumpAndSettle();
        expect(find.text(repo.failure!.message), findsOneWidget);
        expect(find.text('Mã cá biệt: BS0001'), findsNothing);
        repo.failure = null;
        await tester.tap(find.text('Thử lại'));
        await tester.pumpAndSettle();
        expect(find.text('Mã cá biệt: BS0001'), findsOneWidget);
        expect(tester.takeException(), isNull);
        await tester.pumpWidget(const SizedBox.shrink());
      },
    );
  }
  testWidgets('401 clears session and routes back to login', (tester) async {
    final repo = FailedLoansRepository()
      ..failure = const AppError(
        'TOKEN_KHONG_HOP_LE',
        'Hết phiên',
        status: 401,
      );
    final session = await sessionFor(repo);
    await tester.pumpWidget(LibraryApp(session: session));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Đang mượn'));
    await tester.pumpAndSettle();
    expect(find.text('Chào bạn đọc'), findsOneWidget);
    expect(session.current, isNull);
    expect(find.textContaining('Phiên đăng nhập đã hết hạn'), findsOneWidget);
    await tester.pumpWidget(const SizedBox.shrink());
  });
  testWidgets('Empty loans displays empty state and reload action', (
    tester,
  ) async {
    final session = await sessionFor(FailedLoansRepository()..empty = true);
    await tester.pumpWidget(LibraryApp(session: session));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Đang mượn'));
    await tester.pumpAndSettle();
    expect(find.text('Bạn chưa có sách đang mượn.'), findsOneWidget);
    expect(find.text('Tải lại'), findsOneWidget);
    await tester.pumpWidget(const SizedBox.shrink());
  });
  testWidgets(
    'Pending request shows progress and completing after disposal is safe',
    (tester) async {
      final session = await sessionFor(
        MockLibraryRepository(assets: FixtureBundle()),
      );
      final pending = Completer<PageResult<String>>();
      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: PagedList<String>(
              session: session,
              load: (_) => pending.future,
              itemBuilder: Text.new,
              emptyMessage: 'Trống',
            ),
          ),
        ),
      );
      expect(find.byType(CircularProgressIndicator), findsOneWidget);
      await tester.pumpWidget(const SizedBox.shrink());
      pending.complete(
        const PageResult(['old'], page: 1, pageSize: 20, total: 1),
      );
      await tester.pump();
      expect(tester.takeException(), isNull);
    },
  );
  testWidgets('Pagination appends page two instead of replacing page one', (
    tester,
  ) async {
    final session = await sessionFor(
      MockLibraryRepository(assets: FixtureBundle()),
    );
    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: PagedList<String>(
            session: session,
            load: (page) async =>
                PageResult(['Book $page'], page: page, pageSize: 1, total: 2),
            itemBuilder: Text.new,
            emptyMessage: 'Trống',
          ),
        ),
      ),
    );
    await tester.pumpAndSettle();
    await tester.tap(find.text('Xem thêm'));
    await tester.pumpAndSettle();
    expect(find.text('Book 1'), findsOneWidget);
    expect(find.text('Book 2'), findsOneWidget);
    expect(find.text('Xem thêm'), findsNothing);
  });
}
