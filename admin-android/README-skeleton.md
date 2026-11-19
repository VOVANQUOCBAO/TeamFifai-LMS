# admin-android (skeleton tuan 9)

Java/XML, minSdk 24. Chi la khung project, chua co chuc nang.
File nay la ghi chu rieng cua skeleton; README chinh cua nhom la `README.md`.

## Trang thai
- Build: **DA CHAY** ngay 06/11/2026 bang Android Studio (Build > Assemble Project): thanh cong, co canh bao Java 8 loi thoi. Chua chay tren may ao/may that. Chua chay bang `./gradlew`.
- Retrofit/Gson: da khai bao dependency, **chua goi API that**.
- Man hinh: chi co LoginActivity rong (trong `ui/auth`).
- Gradle Wrapper: **CHUA CO** `gradlew` va `gradle-wrapper.jar` (chi co `gradle-wrapper.properties`). Mo bang Android Studio de build.
- Package/applicationId/TAG: theo README nhom (`com.teamfifai.libraryadmin`, TAG `TEAMFIFAI_ADMIN`).
- BASE_URL: placeholder (10.0.2.2:3000 = localhost cua may khi chay emulator), cho nguoi 1 chot.

## Cach mo/build
Android Studio -> Open thu muc nay -> Sync -> Build > Assemble Project.
Khi Android Studio hoi JVM, chon JVM 21 (Gradle 8.7 khong chay voi JVM 25).

## Khung package
data/api, data/dto, data/repository, session, ui/auth, util
(cac thu muc con khac cua ui do nhom tao san, chua dung)