class AppConfig {
  final String mode;
  final Uri baseUrl;
  AppConfig({required this.mode, required String apiBaseUrl})
    : baseUrl = Uri.parse(apiBaseUrl) {
    if (mode != 'mock' && mode != 'live') {
      throw ArgumentError('APP_MODE chỉ nhận mock hoặc live.');
    }
    if (mode == 'live' &&
        (baseUrl.scheme != 'https' ||
            baseUrl.host.isEmpty ||
            !baseUrl.path.endsWith('/api/v1/') ||
            baseUrl.hasQuery ||
            baseUrl.hasFragment)) {
      throw ArgumentError(
        'API_BASE_URL cần là HTTPS và kết thúc bằng /api/v1/.',
      );
    }
  }
  factory AppConfig.fromEnvironment() => AppConfig(
    mode: const String.fromEnvironment('APP_MODE', defaultValue: 'mock'),
    apiBaseUrl: const String.fromEnvironment('API_BASE_URL'),
  );
  bool get isMock => mode == 'mock';
  String get sessionKey => 'teamfifai.session.$mode.$baseUrl';
}
