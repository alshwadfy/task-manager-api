# 📋 Task Manager API

A **production-grade  RESTful backend** for managing task lists an d tasks, built with Spring Boot 4.

> **Secon d Spring Boot project** — demonstrating ent erprise-level patterns: JWT security, paginat ion, comprehensive testing, and Docker contai nerization.

---

## 🧰 Tech Stack

| Layer  | Technology |
| :--- | :--- |
| **Language* * | Java 21 |
| **Framework** | Spring Boot 4 .1.0 |
| **Security** | Spring Security 6 + J WT (JJWT 0.12) |
| **Database** | PostgreSQL  16 |
| **ORM** | Spring Data JPA / Hibernate  |
| **Build Tool** | Apache Maven |
| **Conta inerization** | Docker + Docker Compose |
| * *API Docs** | SpringDoc OpenAPI / Swagger UI  |
| **Testing** | JUnit 5 + Mockito + Spring  MockMvc |

---

## ✨ Features

- 🔐 **JWT  Authentication** — Stateless token-based a uth with BCrypt password hashing
- 📧 **Ema il Verification** — 6-digit code verificati on with 24-hour expiration via SMTP / Spring  Mail
- 📄 **Pagination & Sorting** — Spri ng Data `Pageable` with full page metadata
-  🛡️ **Global Exception Handling** — Cen tralized `@ControllerAdvice` with structured  error responses
- 🗂️ **DTO Layer** — C lean separation between database entities and  API responses via mappers
- ✅ **50 Automat ed Tests** — Unit tests (Mockito) + Web lay er integration tests (MockMvc)
- 🐳 **Docke r Ready** — Multi-stage Dockerfile + Docker  Compose with PostgreSQL orchestration
- 🔒  **Secrets Management** — Environment varia bles via `.env` file, never hardcoded
- 📖  **Swagger UI** — Interactive API documentat ion with JWT authorization support

---

## � ���️ Full Stack Version

This repository co ntains the **backend API only**.

A complete  frontend is available and fully integrated wi th this API:

👉 **[TaskManager Frontend](h ttps://github.com/alshwadfy/TaskManager)**

# ## To run the full stack:
1. Clone and run th is backend first
2. Clone the frontend repo a nd follow its README

---

## 🚀 Quick Star t

### Option 1: Docker Compose (Recommended) 

```bash
git clone https://github.com/alshwa dfy/task-manager-api
cd task-manager-api
cp . env.example .env
docker compose up --build
`` `

App is live at: `http://localhost:8080`

- --

### Option 2: Run Locally

**Prerequisite s:** Java 21+, Maven, PostgreSQL running on p ort 5432

```bash
git clone https://github.co m/alshwadfy/task-manager-api
./mvnw spring-bo ot:run
```

---

## 📖 API Documentation (S wagger UI)

**`http://localhost:8080/swagger- ui.html`**

1. **Register** via `POST /api/au th/register` (receives verification email)
2.  **Verify Email** via `POST /api/auth/verify` 
3. **Login** via `POST /api/auth/login` and  copy your JWT token
4. Click 🔓 **Authorize ** and paste `Bearer <your-token>`
5. Test al l secured endpoints directly!

---

## 🔗 A PI Endpoints

### 🔐 Auth
| Method | Endpoi nt | Description | Auth |
| :--- | :--- | :-- - | :--- |
| `POST` | `/api/auth/register` |  Register a new user & trigger verification em ail | Public |
| `POST` | `/api/auth/verify`  | Verify email with 6-digit code | Public |
|  `POST` | `/api/auth/resend-verification` | R esend verification email code | Public |
| `P OST` | `/api/auth/login` | Login & receive JW T token (verified accounts) | Public |

### � ��� Task Lists
| Method | Endpoint | Descript ion | Auth |
| :--- | :--- | :--- | :--- |
|  `GET` | `/api/task-lists?page=0&size=10` | Ge t paginated task lists | 🔐 JWT |
| `POST`  | `/api/task-lists` | Create a new task list  | 🔐 JWT |
| `GET` | `/api/task-lists/{id}`  | Get a task list by ID | 🔐 JWT |
| `PUT`  | `/api/task-lists/{id}` | Update a task lis t | 🔐 JWT |
| `DELETE` | `/api/task-lists/ {id}` | Delete a task list | 🔐 JWT |

###  ✅ Tasks
| Method | Endpoint | Description |  Auth |
| :--- | :--- | :--- | :--- |
| `GET`  | `/api/task-lists/{listId}/tasks?page=0&siz e=10` | Get paginated tasks | 🔐 JWT |
| `P OST` | `/api/task-lists/{listId}/tasks` | Cre ate a task | 🔐 JWT |
| `GET` | `/api/task- lists/{listId}/tasks/{taskId}` | Get a task b y ID | 🔐 JWT |
| `PUT` | `/api/task-lists/ {listId}/tasks/{taskId}` | Update a task | � �� JWT |
| `DELETE` | `/api/task-lists/{listI d}/tasks/{taskId}` | Delete a task | 🔐 JWT  |

---

## 🧪 Running Tests

```bash
./mvn w test
```

**50 tests across 6 test suites:* *
- `AuthControllerTest` — 8 MockMvc tests
 - `TaskListControllerTest` — 7 MockMvc test s
- `TasksControllerTest` — 6 MockMvc tests 
- `UserServiceImplTest` — 19 Mockito unit  tests
- `VerificationTokenServiceImplTest` � � 7 Mockito unit tests
- `EmailServiceImplTes t` — 2 Mockito unit tests
- `TasksApplicati onTests` — 1 context / smoke test

---

##  🗂️ Project Structure

```
src/
├──  main/java/com/projects/Tasks/
│   ├─� � config/
│   ├── Controller/
│   � ��── domain/
│   │   ├── dto/
� ��   │   └── entities/
│   ├─� � exceptions/
│   ├── mappers/
│    ├── repositories/
│   ├── secur ity/
│   └── services/
└── test /java/com/projects/Tasks/
    ├── Contr oller/
    └── Services/
```

---

## � ��� Docker Architecture

```
┌────� ��──────────────� ��─────────────┐
 │         docker compose          │
│   ┌────────────── ────────────┐   │ 
│  │  app (Spring Boot)       │◄─� ��┼── REST Requests
│  │  Multi-sta ge Docker Build │  │
│  └──── ──────┬──────── ───────┘   │
│              ▼ (healthcheck)     │
│  ┌─── ─────────────── ────────┐   │
│  │  d b (PostgreSQL 16)      │   │
│  │  Vo lume: postgres_data   │   │
│  └─� �──────────────� �─────────┘   │
└─� ��──────────────� ��──────────────� ��─┘
```

Multi-stage build: **~800MB →  ~180MB**

---

## 🔒 Security Configuratio n

```bash
cp .env.example .env

POSTGRES_DB= taskmanager
POSTGRES_USER=postgres
POSTGRES_P ASSWORD=your_secure_password
JWT_SECRET=your_ jwt_secret_key
MAIL_USERNAME=your-email@gmail .com
MAIL_PASSWORD=your-app-specific-password 
```

---

## 📌 Environment Variables

| V ariable | Description |
| :--- | :--- |
| `SP RING_DATASOURCE_URL` | PostgreSQL JDBC URL |
 | `SPRING_DATASOURCE_USERNAME` | Database use rname |
| `SPRING_DATASOURCE_PASSWORD` | Data base password |
| `SPRING_JPA_HIBERNATE_DDL_A UTO` | Schema strategy |
| `JWT_SECRET` | Sec ret key for JWT signing |
| `MAIL_USERNAME` |  SMTP username / sender email |
| `MAIL_PASSW ORD` | SMTP password / app password |
 