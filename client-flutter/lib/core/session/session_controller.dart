import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../data/models/library_models.dart';
import '../../data/repositories/library_repository.dart';
import '../config/app_config.dart';
import '../errors/app_error.dart';

class SessionController extends ChangeNotifier {
  final SharedPreferences prefs;
  final AppConfig config;
  final LibraryRepository repository;
  LoginResult? current;
  String? notice;
  SessionController(this.prefs, this.config, this.repository);
  bool _expired(String token) {
    if (config.isMock) return false;
    try {
      final parts = token.split('.');
      if (parts.length != 3) return true;
      final payload =
          jsonDecode(
                utf8.decode(base64Url.decode(base64Url.normalize(parts[1]))),
              )
              as Json;
      return (payload['exp'] as num) * 1000 <=
          DateTime.now().millisecondsSinceEpoch;
    } catch (_) {
      return true;
    }
  }

  Future<void> restore() async {
    final stored = prefs.getString(config.sessionKey);
    if (stored == null) return;
    try {
      final saved = LoginResult.fromJson(jsonDecode(stored) as Json);
      if (saved.nguoiDung.vaiTro != 'BAN_DOC' || _expired(saved.token)) {
        await logout(expired: true);
      } else {
        current = saved;
      }
    } catch (_) {
      await prefs.remove(config.sessionKey);
    }
  }

  Future<void> login(String email, String password) async {
    final result = await repository.login(email.trim(), password);
    if (result.nguoiDung.vaiTro != 'BAN_DOC') {
      throw const AppError(
        'KHONG_CO_QUYEN',
        'Ứng dụng này dành cho tài khoản bạn đọc.',
        status: 403,
      );
    }
    if (_expired(result.token)) {
      throw const AppError(
        'TOKEN_KHONG_HOP_LE',
        'Máy chủ trả về token không hợp lệ.',
        status: 401,
      );
    }
    if (!await prefs.setString(
      config.sessionKey,
      jsonEncode(result.toJson()),
    )) {
      throw const AppError(
        'STORAGE',
        'Không lưu được phiên đăng nhập. Vui lòng thử lại.',
      );
    }
    current = result;
    notice = null;
    notifyListeners();
  }

  Future<void> logout({bool expired = false}) async {
    await prefs.remove(config.sessionKey);
    current = null;
    notice = expired
        ? 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.'
        : null;
    notifyListeners();
  }

  Future<void> checkExpiry() async {
    if (current != null && _expired(current!.token))
      await logout(expired: true);
  }
}
