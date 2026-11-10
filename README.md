# OULMS — Hệ quản lý học liệu số

Web quản lý học liệu số (Learning Management System) cho trường mở: backend Java Servlet
+ frontend React, xác thực qua Google OAuth.

## Stack — đọc từ `backend/pom.xml` và `frontend/package.json`

**Backend** (Maven, đóng gói `war` — chạy trên servlet container, không phải Spring Boot):

```
groupId com.qtk · artifactId backend · version 1.0-SNAPSHOT · packaging war
jakartaee   11.0.0-M1
spring      6.2.17        (Spring Framework, không phải Boot)
junit       5.11.0 · mockito 5.12.0
```

**Frontend** (React 19 + Vite):

```
react 19.2.6 · react-bootstrap 2.10.10 · bootstrap 5.3.8 · react-bootstrap-icons
axios 1.16.1
@react-oauth/google 0.13.5
@testing-library/{dom,jest-dom,react,user-event}
```

## Cấu trúc

```
backend/
    pom.xml
    nb-configuration.xml           ← cấu hình NetBeans
    src/main/java/com/...          mã Java
    src/main/resources/            static, templates
    src/main/webapp/WEB-INF/       web.xml

frontend/
    package.json · package-lock.json
    src/
        configs/api/               gọi API (axios)
        screens/Authentication/    đăng nhập
        screens/Community/         cộng đồng
        components/ · reducers/    UI + state

lmsdb.sql                          schema / dữ liệu mẫu
```

## Chạy

**Backend** — cần servlet container (Tomcat/WildFly) để deploy `war`:

```bash
cd backend && mvn clean package
# deploy target/backend-1.0-SNAPSHOT.war lên container
```

**Frontend**:

```bash
cd frontend && npm install && npm run dev
```

**CSDL**: nạp `lmsdb.sql` trước khi chạy backend.

## Nhánh

| Nhánh | Việc |
|:--|:--|
| `develop` | **nhánh đang làm** (mặc định hiện tại) |
| `release` | nhánh phát hành |

## Ghi chú

- Backend là **`war` + Spring Framework 6.2**, không phải Spring Boot. Không có
  `@SpringBootApplication`; cấu hình truyền thống qua `web.xml`.
- `frontend/node_modules/` và `frontend/build/` **đang có trong cây làm việc** — kiểm
  `.gitignore` để chắc chúng không bị commit.
- Không tìm thấy cấu hình Keycloak trong repo dù đã tìm; xác thực dùng Google OAuth phía
  frontend.

## Giấy phép

MIT (xem `LICENSE`).
