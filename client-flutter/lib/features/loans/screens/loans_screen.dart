import 'package:flutter/material.dart';
import '../../../core/session/session_controller.dart';
import '../../../data/models/library_models.dart';
import '../../../shared/widgets/paged_list.dart';

String vietnamDate(DateTime utc) {
  final date = utc.toUtc().add(const Duration(hours: 7));
  return '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year} '
      '${date.hour.toString().padLeft(2, '0')}:${date.minute.toString().padLeft(2, '0')}';
}

class LoansScreen extends StatelessWidget {
  final SessionController session;
  const LoansScreen({super.key, required this.session});
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
              'Sách đang mượn',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            const SizedBox(height: 8),
            const Text('Kéo xuống để cập nhật. Hạn trả theo giờ Việt Nam.'),
          ],
        ),
      ),
      Expanded(
        child: PagedList<Loan>(
          session: session,
          load: (page) =>
              session.repository.myLoans(session.current!.token, page: page),
          emptyMessage: 'Bạn chưa có sách đang mượn.',
          itemBuilder: (loan) => Card(
            margin: const EdgeInsets.only(bottom: 12),
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    loan.nhanDe,
                    style: Theme.of(context).textTheme.titleMedium,
                  ),
                  const SizedBox(height: 8),
                  Text('Mã cá biệt: ${loan.maCaBiet}'),
                  const SizedBox(height: 12),
                  Text('Hạn trả: ${vietnamDate(loan.hanTra)}'),
                  const SizedBox(height: 12),
                  Chip(
                    label: Text(
                      loan.trangThai == 'DANG_MUON' ? 'Đang mượn' : 'Đã trả',
                    ),
                  ),
                  if (loan.trangThai == 'DANG_MUON' &&
                      loan.hanTra.isBefore(DateTime.now().toUtc()))
                    Text(
                      'Đã quá hạn trả',
                      style: TextStyle(
                        color: Theme.of(context).colorScheme.error,
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
}
