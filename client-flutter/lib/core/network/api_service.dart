import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import '../../data/models/library_models.dart';
import '../errors/app_error.dart';

class ApiService {
  final Uri baseUrl;
  final http.Client client;
  final Duration timeout;
  ApiService(
    this.baseUrl,
    this.client, {
    this.timeout = const Duration(seconds: 90),
  });

  Future<Json> request(
    String path, {
    String? token,
    Json? body,
    Map<String, String>? query,
  }) async {
    final uri = baseUrl.resolve(path).replace(queryParameters: query);
    final headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      if (token != null) 'Authorization': 'Bearer $token',
    };
    try {
      final response =
          await (body == null
                  ? client.get(uri, headers: headers)
                  : client.post(uri, headers: headers, body: jsonEncode(body)))
              .timeout(timeout);
      Json? json;
      try {
        final decoded = jsonDecode(utf8.decode(response.bodyBytes));
        if (decoded is Json) json = decoded;
      } on FormatException {
        /* A proxy can return HTML; still handle HTTP status. */
      }
      if (response.statusCode < 200 || response.statusCode >= 300) {
        final error = json?['error'];
        final code = error is Json ? error['code'] as String? : null;
        final message = error is Json ? error['message'] as String? : null;
        throw AppError(
          code ?? 'HTTP_${response.statusCode}',
          response.statusCode == 501
              ? 'Chức năng chưa tích hợp'
              : message ??
                    switch (response.statusCode) {
                      401 =>
                        'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.',
                      403 => 'Bạn không có quyền thực hiện thao tác này.',
                      _ =>
                        'Máy chủ gặp lỗi (${response.statusCode}). Vui lòng thử lại.',
                    },
          status: response.statusCode,
        );
      }
      if (json == null) {
        throw const AppError(
          'INVALID_RESPONSE',
          'Dữ liệu máy chủ không hợp lệ.',
        );
      }
      return json;
    } on TimeoutException {
      throw const AppError(
        'TIMEOUT',
        'Máy chủ phản hồi chậm. Vui lòng thử lại.',
      );
    } on SocketException {
      throw const AppError(
        'NETWORK',
        'Không kết nối được máy chủ. Kiểm tra mạng và thử lại.',
      );
    } on http.ClientException {
      throw const AppError(
        'NETWORK',
        'Không kết nối được máy chủ. Kiểm tra mạng và thử lại.',
      );
    }
  }
}
