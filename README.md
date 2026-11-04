# OULMS — Open University Learning Material System

A learning-material management system: courses, subjects, levels, documents, a forum,
quizzes and paid enrolment. Built as a university course project.

- **Backend** — Java 21 · Spring MVC 6.2 on Jakarta EE 11 (WAR) · Hibernate · MySQL · JWT ·
  Spring Security · Thymeleaf for the admin/report pages · SpringDoc for OpenAPI
- **Frontend** — React 19 (`react-scripts`) · react-router 7 · react-bootstrap · axios

## Architecture

Request flow, one layer per hop — no layer is skipped:

```
HTTP ─► Controller  ─► Service (interface) ─► ServiceImpl ─► Repository (interface)
                                                              └─► RepositoryImpl ─► Hibernate ─► MySQL
```

```
backend/src/main/java/com/qtk/
  controllers/     REST controllers (Document, Course, Subject, Topic, Tag, Comment,
                   ForumPost, Payment, User, Auth, Chart, Report) + MVC page controllers
  services/        interfaces          impl/   implementations
  repositories/    interfaces          impl/   implementations
  pojo/            19 Hibernate entities (Course, Document, ForumPost, Quizzes, Transaction, …)
  dto/             15 request/response records — entities never leave the API
  configs/         Security, JWT filter wiring, Hibernate, Thymeleaf, OpenAPI, Google Drive
  filters/         JwtFilter — authenticates each request from the bearer token
  utils/           JwtUtils, HmacUtils (payment signature)
  formatters/      Level / Subject / Topic converters
  enums/           UserRole, DocumentStatus, TransactionStatus, TransactionPaymentMethod
  test/            TestConnection — manual DB connectivity check
```

Two layers exist purely as seams: **DTOs** keep entities out of the API payloads, and the
**repository interface + `impl` split** keeps Hibernate out of the service layer. The
`impl` packages are where the actual Hibernate queries live.

Notable pieces:

- **Auth** — JWT issued on login, validated by `JwtFilter`, plus Google OAuth
  (`GoogleLoginRequestDTO` / `GoogleDriveConfig`). Google Drive is used as document storage.
- **Payments** — `PaymentService` + `ZaloPayServiceImpl`; request signatures via `HmacUtils`.
- **Learning data** — `Progress`, `CourseStudent`, `Question`/`Quizzes`/`QuizzesResult`/
  `StudentAnswer` model the quiz-taking path.

## Frontend

```
frontend/src/
  screens/    Authentication · Home · Explore · Courses · MyLearning · DocumentDetail ·
              DocumentManager · Community · Payment
  components/ reducers/ configs/ asserts/
```

Screens are organised by feature; `reducers/` holds the shared state.

## Database

`lmsdb.sql` at the repository root is the schema + seed dump — import it before running
the backend:

```bash
mysql -u <user> -p < lmsdb.sql
```

## Running

**Backend** — a WAR built with Maven and deployed to a servlet container (Tomcat 10+ for
Jakarta EE 11):

```bash
cd backend
mvn package                 # → target/backend-1.0-SNAPSHOT.war
```

**Frontend:**

```bash
cd frontend
npm install
npm start
```

## CI

`.github/workflows/blank.yml` — a "Spring MVC CI" job that builds `backend/` on pushes and
pulls to the `develop` branch. The filename is the GitHub default and was never renamed.

## License

MIT — see `LICENSE`.
