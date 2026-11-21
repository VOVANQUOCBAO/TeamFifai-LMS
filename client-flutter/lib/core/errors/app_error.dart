class AppError implements Exception {
  final String code;
  final String message;
  final int? status;
  const AppError(this.code, this.message, {this.status});
  bool get isExpired => status == 401 && code != 'SAI_THONG_TIN_DANG_NHAP';
  @override
  String toString() => message;
}
