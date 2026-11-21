import 'package:flutter/material.dart';
import '../../core/errors/app_error.dart';
import '../../core/session/session_controller.dart';
import '../../data/models/library_models.dart';

class PagedList<T> extends StatefulWidget {
  final Future<PageResult<T>> Function(int page) load;
  final Widget Function(T item) itemBuilder;
  final String emptyMessage;
  final SessionController session;
  const PagedList({
    super.key,
    required this.load,
    required this.itemBuilder,
    required this.emptyMessage,
    required this.session,
  });
  @override
  State<PagedList<T>> createState() => _PagedListState<T>();
}

class _PagedListState<T> extends State<PagedList<T>> {
  final List<T> _items = [];
  bool _loading = true, _more = false;
  int _page = 0, _generation = 0;
  String? _error;
  @override
  void initState() {
    super.initState();
    _load(reset: true);
  }

  Future<void> _load({bool reset = false}) async {
    final generation = ++_generation;
    final token = widget.session.current?.token;
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final result = await widget.load(reset ? 1 : _page + 1);
      if (!mounted ||
          generation != _generation ||
          widget.session.current?.token != token) {
        return;
      }
      setState(() {
        if (reset) _items.clear();
        _items.addAll(result.data);
        _page = result.page;
        _more = result.hasMore;
      });
    } on AppError catch (e) {
      if (!mounted ||
          generation != _generation ||
          widget.session.current?.token != token) {
        return;
      }
      if (e.isExpired) {
        await widget.session.logout(expired: true);
      } else {
        setState(() => _error = e.message);
      }
    } catch (_) {
      if (mounted && generation == _generation) {
        setState(() => _error = 'Không tải được dữ liệu. Vui lòng thử lại.');
      }
    } finally {
      if (mounted && generation == _generation) {
        setState(() => _loading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) => RefreshIndicator(
    onRefresh: () => _load(reset: true),
    child: ListView.builder(
      physics: const AlwaysScrollableScrollPhysics(),
      padding: const EdgeInsets.fromLTRB(20, 8, 20, 24),
      itemCount: _items.length + 1,
      itemBuilder: (context, index) {
        if (index < _items.length) return widget.itemBuilder(_items[index]);
        if (_loading) {
          return const Padding(
            padding: EdgeInsets.all(32),
            child: Center(child: CircularProgressIndicator()),
          );
        }
        if (_error != null) {
          return Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              children: [
                const Icon(Icons.cloud_off_outlined, size: 40),
                const SizedBox(height: 12),
                Text(_error!, textAlign: TextAlign.center),
                const SizedBox(height: 12),
                OutlinedButton(
                  onPressed: () => _load(reset: _items.isEmpty),
                  child: const Text('Thử lại'),
                ),
              ],
            ),
          );
        }
        if (_items.isEmpty) {
          return Padding(
            padding: const EdgeInsets.all(40),
            child: Column(
              children: [
                const Icon(Icons.auto_stories_outlined, size: 48),
                const SizedBox(height: 16),
                Text(widget.emptyMessage, textAlign: TextAlign.center),
                TextButton(
                  onPressed: () => _load(reset: true),
                  child: const Text('Tải lại'),
                ),
              ],
            ),
          );
        }
        return Padding(
          padding: const EdgeInsets.only(top: 12),
          child: Center(
            child: _more
                ? OutlinedButton(
                    onPressed: () => _load(),
                    child: const Text('Xem thêm'),
                  )
                : TextButton(
                    onPressed: () => _load(reset: true),
                    child: const Text('Tải lại'),
                  ),
          ),
        );
      },
    ),
  );
}
