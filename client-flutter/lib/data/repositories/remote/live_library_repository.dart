import '../../../core/network/api_service.dart';
import '../../../core/errors/app_error.dart';
import '../../models/library_models.dart';
import '../library_repository.dart';

class LiveLibraryRepository implements LibraryRepository {
  final ApiService api;
  LiveLibraryRepository(this.api);
  Future<T> _parse<T>(Future<Json> request, T Function(Json) parse) async {
    final json = await request;
    try {
      return parse(json);
    } on FormatException {
      throw const AppError(
        'INVALID_RESPONSE',
        'Dữ liệu máy chủ không đúng hợp đồng.',
      );
    } on TypeError {
      throw const AppError(
        'INVALID_RESPONSE',
        'Dữ liệu máy chủ không đúng hợp đồng.',
      );
    }
  }

  @override
  Future<LoginResult> login(String email, String password) => _parse(
    api.request('auth/dang-nhap', body: {'email': email, 'matKhau': password}),
    (j) => LoginResult.fromJson(j['data'] as Json),
  );
  @override
  Future<PageResult<Book>> books(
    String token, {
    String keyword = '',
    int page = 1,
  }) => _parse(
    api.request(
      'sach',
      token: token,
      query: {'tuKhoa': keyword, 'page': '$page', 'pageSize': '20'},
    ),
    (j) => PageResult.fromJson(j, Book.fromJson),
  );
  @override
  Future<PageResult<Loan>> myLoans(String token, {int page = 1}) => _parse(
    api.request(
      'ban-doc/toi/dang-muon',
      token: token,
      query: {'page': '$page', 'pageSize': '20'},
    ),
    (j) => PageResult.fromJson(j, Loan.fromJson),
  );
}
