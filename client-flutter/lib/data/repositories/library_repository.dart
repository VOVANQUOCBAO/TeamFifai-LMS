import '../models/library_models.dart';

abstract class LibraryRepository {
  Future<LoginResult> login(String email, String password);
  Future<PageResult<Book>> books(
    String token, {
    String keyword = '',
    int page = 1,
  });
  Future<PageResult<Loan>> myLoans(String token, {int page = 1});
}
