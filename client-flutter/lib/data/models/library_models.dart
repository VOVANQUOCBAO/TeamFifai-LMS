typedef Json = Map<String, dynamic>;

class ReaderUser {
  final String id, hoTen, vaiTro;
  const ReaderUser(this.id, this.hoTen, this.vaiTro);
  factory ReaderUser.fromJson(Json j) => ReaderUser(
    j['id'] as String,
    j['hoTen'] as String,
    j['vaiTro'] as String,
  );
  Json toJson() => {'id': id, 'hoTen': hoTen, 'vaiTro': vaiTro};
}

class LoginResult {
  final String token;
  final ReaderUser nguoiDung;
  const LoginResult(this.token, this.nguoiDung);
  factory LoginResult.fromJson(Json j) => LoginResult(
    j['token'] as String,
    ReaderUser.fromJson(j['nguoiDung'] as Json),
  );
  Json toJson() => {'token': token, 'nguoiDung': nguoiDung.toJson()};
}

class Book {
  final String id, nhanDe, tacGia, phanLoaiId;
  final String? nxb, isbn, anhBia;
  final int? namXb;
  final int soBanRanh;
  Book.fromJson(Json j)
    : id = j['id'] as String,
      nhanDe = j['nhanDe'] as String,
      tacGia = j['tacGia'] as String,
      phanLoaiId = j['phanLoaiId'] as String,
      nxb = j['nxb'] as String?,
      isbn = j['isbn'] as String?,
      anhBia = j['anhBia'] as String?,
      namXb = j['namXb'] as int?,
      soBanRanh = j['soBanRanh'] as int;
}

class Loan {
  final String id, banDocId, banSachId, nhanDe, maCaBiet, trangThai;
  final DateTime muonLuc, hanTra;
  Loan.fromJson(Json j)
    : id = j['id'] as String,
      banDocId = j['banDocId'] as String,
      banSachId = j['banSachId'] as String,
      nhanDe = j['nhanDe'] as String,
      maCaBiet = j['maCaBiet'] as String,
      trangThai = j['trangThai'] as String,
      muonLuc = DateTime.parse(j['muonLuc'] as String).toUtc(),
      hanTra = DateTime.parse(j['hanTra'] as String).toUtc();
}

class PageResult<T> {
  final List<T> data;
  final int page, pageSize, total;
  const PageResult(
    this.data, {
    required this.page,
    required this.pageSize,
    required this.total,
  });
  factory PageResult.fromJson(Json j, T Function(Json) parse) {
    final meta = j['meta'] as Json;
    return PageResult(
      (j['data'] as List).map((e) => parse(e as Json)).toList(),
      page: meta['page'] as int,
      pageSize: meta['pageSize'] as int,
      total: meta['total'] as int,
    );
  }
  bool get hasMore => page * pageSize < total;
}
