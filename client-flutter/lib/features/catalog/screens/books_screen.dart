import 'package:flutter/material.dart';
import '../../../core/session/session_controller.dart';
import '../../../data/models/library_models.dart';
import '../../../shared/widgets/paged_list.dart';

class BooksScreen extends StatefulWidget {
  final SessionController session;
  const BooksScreen({super.key, required this.session});
  @override
  State<BooksScreen> createState() => _BooksScreenState();
}

class _BooksScreenState extends State<BooksScreen> {
  final _search = TextEditingController();
  String _keyword = '';
  @override
  void dispose() {
    _search.dispose();
    super.dispose();
  }

  void _submit() {
    FocusScope.of(context).unfocus();
    setState(() => _keyword = _search.text.trim());
  }

  @override
  Widget build(BuildContext context) => Column(
    crossAxisAlignment: CrossAxisAlignment.stretch,
    children: [
      Padding(
        padding: const EdgeInsets.fromLTRB(20, 12, 20, 16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Khám phá thư viện',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            const SizedBox(height: 8),
            const Text('Tra cứu nhan đề, tác giả và số bản sẵn sàng.'),
            const SizedBox(height: 20),
            TextField(
              controller: _search,
              onSubmitted: (_) => _submit(),
              textInputAction: TextInputAction.search,
              decoration: InputDecoration(
                hintText: 'Tìm nhan đề hoặc tác giả',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: IconButton(
                  tooltip: 'Tìm kiếm',
                  onPressed: _submit,
                  icon: const Icon(Icons.arrow_forward),
                ),
              ),
            ),
          ],
        ),
      ),
      Expanded(
        child: PagedList<Book>(
          key: ValueKey(_keyword),
          session: widget.session,
          load: (page) => widget.session.repository.books(
            widget.session.current!.token,
            keyword: _keyword,
            page: page,
          ),
          emptyMessage: 'Không tìm thấy sách phù hợp. Thử từ khóa khác nhé.',
          itemBuilder: (book) => Card(
            margin: const EdgeInsets.only(bottom: 12),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  ClipRRect(
                    borderRadius: BorderRadius.circular(8),
                    child: SizedBox(
                      width: 64,
                      height: 88,
                      child: book.anhBia == null
                          ? _cover(context)
                          : Image.network(
                              book.anhBia!,
                              fit: BoxFit.cover,
                              loadingBuilder: (_, child, progress) =>
                                  progress == null
                                  ? child
                                  : const Center(
                                      child: CircularProgressIndicator(),
                                    ),
                              errorBuilder: (_, error, stack) =>
                                  _cover(context),
                            ),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          book.nhanDe,
                          style: Theme.of(context).textTheme.titleMedium,
                        ),
                        const SizedBox(height: 6),
                        Text(book.tacGia),
                        const SizedBox(height: 10),
                        Text(
                          book.soBanRanh > 0
                              ? '${book.soBanRanh} bản sẵn sàng'
                              : 'Hiện chưa có bản rảnh',
                          style: TextStyle(
                            color: book.soBanRanh > 0
                                ? Theme.of(context).colorScheme.primary
                                : Theme.of(context).colorScheme.error,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    ],
  );
  Widget _cover(BuildContext context) => ColoredBox(
    color: Theme.of(context).colorScheme.primaryContainer,
    child: const Icon(Icons.menu_book_outlined, size: 32),
  );
}
