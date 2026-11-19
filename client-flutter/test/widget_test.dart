import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:teamfifai_client/core/config/app_config.dart';
import 'package:teamfifai_client/core/session/session_controller.dart';
import 'package:teamfifai_client/data/repositories/mock/mock_library_repository.dart';
import 'package:teamfifai_client/main.dart';
import 'package:flutter/material.dart';

void main() {
  testWidgets('Mock reader logs in, searches, views loans and logs out', (
    tester,
  ) async {
    SharedPreferences.setMockInitialValues({});
    final session = SessionController(
      await SharedPreferences.getInstance(),
      AppConfig(mode: 'mock', apiBaseUrl: ''),
      MockLibraryRepository(),
    );
    await tester.pumpWidget(LibraryApp(session: session));
    await tester.tap(find.text('Đăng nhập'));
    await tester.pumpAndSettle();
    expect(find.text('Nhập email hợp lệ.'), findsOneWidget);
    await tester.enterText(
      find.byType(TextFormField).at(0),
      'bandoc@thuviendemo.test',
    );
    await tester.enterText(find.byType(TextFormField).at(1), 'DemoLib!4211');
    await tester.tap(find.text('Đăng nhập'));
    await tester.pumpAndSettle();
    expect(find.text('Dế Mèn phiêu lưu ký'), findsOneWidget);
    expect(find.text('DEMO'), findsOneWidget);
    await tester.enterText(find.byType(TextField), 'khong-co-sach-nay');
    await tester.tap(find.byTooltip('Tìm kiếm'));
    await tester.pumpAndSettle();
    expect(find.textContaining('Không tìm thấy sách'), findsOneWidget);
    await tester.tap(find.text('Đang mượn'));
    await tester.pumpAndSettle();
    expect(find.text('Mã cá biệt: BS0001'), findsOneWidget);
    expect(find.text('Hạn trả: 29/11/2026 10:00'), findsOneWidget);
    await tester.tap(find.byTooltip('Đăng xuất'));
    await tester.pumpAndSettle();
    expect(find.text('Chào bạn đọc'), findsOneWidget);
    expect(session.current, isNull);
    expect(find.text('Mã cá biệt: BS0001'), findsNothing);
    await tester.pumpWidget(const SizedBox.shrink());
    session.dispose();
  });
}
