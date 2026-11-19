import 'dart:convert';
import 'package:flutter/services.dart';
import '../../../core/errors/app_error.dart';
import '../../models/library_models.dart';
import '../library_repository.dart';

class MockLibraryRepository implements LibraryRepository {
  final AssetBundle assets;
  MockLibraryRepository({AssetBundle? assets}) : assets = assets ?? rootBundle;
  Future<Json> _read(String name) async =>
      jsonDecode(await assets.loadString('assets/$name')) as Json;
  Future<Json> _fixture(String name) => _read('fixtures/$name.json');
  Future<LoginResult> _reader(String token) async {
    final user = LoginResult.fromJson(
      (await _fixture('login-ban_doc'))['data'] as Json,
    );
    if (token != user.token) {
      throw const AppError(
        'TOKEN_KHONG_HOP_LE',
        'Phiên đăng nhập không hợp lệ.',
        status: 401,
      );
    }
    return user;
  }

  @override
  Future<LoginResult> login(String email, String password) async {
    final accounts = (await _read('demo-ids.json'))['demoAccounts'] as List;
    final matching = accounts.where(
      (a) => a['email'] == email && a['matKhau'] == password,
    );
    if (matching.isEmpty) {
      throw const AppError(
        'SAI_THONG_TIN_DANG_NHAP',
        'Email hoặc mật khẩu không đúng.',
        status: 401,
      );
    }
    final account = matching.first as Json;
    if (account['vaiTro'] != 'BAN_DOC') {
      throw const AppError(
        'KHONG_CO_QUYEN',
        'Ứng dụng này dành cho tài khoản bạn đọc.',
        status: 403,
      );
    }
    return LoginResult.fromJson(
      (await _fixture('login-ban_doc'))['data'] as Json,
    );
  }

  PageResult<T> _page<T>(List<T> all, int page) => PageResult(
    all.skip((page - 1) * 20).take(20).toList(),
    page: page,
    pageSize: 20,
    total: all.length,
  );
  @override
  Future<PageResult<Book>> books(
    String token, {
    String keyword = '',
    int page = 1,
  }) async {
    await _reader(token);
    final all = PageResult.fromJson(
      await _fixture('books-after-loan'),
      Book.fromJson,
    ).data;
    final term = keyword.trim().toLowerCase();
    return _page(
      all
          .where((b) => '${b.nhanDe} ${b.tacGia}'.toLowerCase().contains(term))
          .toList(),
      page,
    );
  }

  @override
  Future<PageResult<Loan>> myLoans(String token, {int page = 1}) async {
    final user = await _reader(token);
    final accounts = (await _read('demo-ids.json'))['demoAccounts'] as List;
    final account = accounts.cast<Json>().singleWhere(
      (a) => a['userId'] == user.nguoiDung.id,
    );
    final all = PageResult.fromJson(
      await _fixture('my-loans-after-loan'),
      Loan.fromJson,
    ).data;
    return _page(
      all.where((loan) => loan.banDocId == account['banDocId']).toList(),
      page,
    );
  }
}
