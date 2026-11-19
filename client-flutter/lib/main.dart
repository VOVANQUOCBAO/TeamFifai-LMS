import 'dart:async';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'core/config/app_config.dart';
import 'core/network/api_service.dart';
import 'core/session/session_controller.dart';
import 'data/repositories/mock/mock_library_repository.dart';
import 'data/repositories/remote/live_library_repository.dart';
import 'features/auth/screens/login_screen.dart';
import 'features/catalog/screens/books_screen.dart';
import 'features/loans/screens/loans_screen.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  try {
    final config = AppConfig.fromEnvironment();
    final repository = config.isMock
        ? MockLibraryRepository()
        : LiveLibraryRepository(ApiService(config.baseUrl, http.Client()));
    final session = SessionController(
      await SharedPreferences.getInstance(),
      config,
      repository,
    );
    await session.restore();
    runApp(LibraryApp(session: session));
  } catch (_) {
    runApp(
      const MaterialApp(
        home: Scaffold(
          body: Center(
            child: Padding(
              padding: EdgeInsets.all(24),
              child: Text(
                'Không thể khởi động. Kiểm tra APP_MODE (mock/live), API_BASE_URL HTTPS kết thúc /api/v1/ và bộ nhớ thiết bị.',
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class LibraryApp extends StatefulWidget {
  final SessionController session;
  const LibraryApp({super.key, required this.session});
  @override
  State<LibraryApp> createState() => _LibraryAppState();
}

class _LibraryAppState extends State<LibraryApp> with WidgetsBindingObserver {
  Timer? _expiryTimer;
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _expiryTimer = Timer.periodic(
      const Duration(seconds: 30),
      (_) => widget.session.checkExpiry(),
    );
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) widget.session.checkExpiry();
  }

  @override
  void dispose() {
    _expiryTimer?.cancel();
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => MaterialApp(
    title: 'TeamFifai Library',
    debugShowCheckedModeBanner: false,
    theme: ThemeData(
      useMaterial3: true,
      colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF176B5B)),
      scaffoldBackgroundColor: const Color(0xFFF6F8F5),
      inputDecorationTheme: const InputDecorationTheme(
        border: OutlineInputBorder(),
        filled: true,
        fillColor: Colors.white,
      ),
      appBarTheme: const AppBarTheme(
        centerTitle: false,
        backgroundColor: Color(0xFFF6F8F5),
      ),
    ),
    home: ListenableBuilder(
      listenable: widget.session,
      builder: (context, _) {
        // Replacing the Navigator removes every private route when the session ends.
        final user = widget.session.current?.nguoiDung.id;
        return Navigator(
          key: ValueKey(user ?? 'signed-out'),
          onGenerateRoute: (_) => MaterialPageRoute<void>(
            builder: (_) => user == null
                ? LoginScreen(session: widget.session)
                : ReaderHome(session: widget.session),
          ),
        );
      },
    ),
  );
}

class ReaderHome extends StatefulWidget {
  final SessionController session;
  const ReaderHome({super.key, required this.session});
  @override
  State<ReaderHome> createState() => _ReaderHomeState();
}

class _ReaderHomeState extends State<ReaderHome> {
  int _selected = 0;
  bool _loggingOut = false;
  Future<void> _logout() async {
    setState(() => _loggingOut = true);
    try {
      await widget.session.logout();
    } catch (_) {
      if (!mounted) return;
      setState(() => _loggingOut = false);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Không xóa được phiên. Vui lòng thử lại.'),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: const Text('TeamFifai Library'),
      actions: [
        if (widget.session.config.isMock) const Chip(label: Text('DEMO')),
        IconButton(
          tooltip: 'Đăng xuất',
          onPressed: _loggingOut ? null : _logout,
          icon: const Icon(Icons.logout),
        ),
      ],
    ),
    body: SafeArea(
      child: _selected == 0
          ? BooksScreen(session: widget.session)
          : LoansScreen(session: widget.session),
    ),
    bottomNavigationBar: NavigationBar(
      selectedIndex: _selected,
      onDestinationSelected: (i) => setState(() => _selected = i),
      destinations: const [
        NavigationDestination(
          icon: Icon(Icons.menu_book_outlined),
          selectedIcon: Icon(Icons.menu_book),
          label: 'Tra cứu sách',
        ),
        NavigationDestination(
          icon: Icon(Icons.bookmark_outline),
          selectedIcon: Icon(Icons.bookmark),
          label: 'Đang mượn',
        ),
      ],
    ),
  );
}
