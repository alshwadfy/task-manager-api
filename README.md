# 📋 Task Manager API

A **production-grade RESTful backend** for managing task lists and tasks, built with Spring Boot 4.

> **Second Spring Boot project** — demonstrating enterprise-level patterns: JWT security, pagination, comprehensive testing, and Docker containerization.

---

## 🧰 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.1.0 |
| **Security** | Spring Security 6 + JWT (JJWT 0.12) |
| **Database** | PostgreSQL 16 |
| **ORM** | Spring Data JPA / Hibernate |
| **Build Tool** | Apache Maven |
| **Containerization** | Docker + Docker Compose |
| **API Docs** | SpringDoc OpenAPI / Swagger UI |
| **Testing** | JUnit 5 + Mockito + Spring MockMvc |

---

## ✨ Features

- 🔐 **JWT Authentication** — Stateless token-based auth with BCrypt password hashing
- 📧 **Email Verification** — 6-digit code verification with 24-hour expiration via SMTP / Spring Mail
- 📄 **Pagination & Sorting** — Spring Data `Pageable` with full page metadata
- 🛡️ **Global Exception Handling** — Centralized `@ControllerAdvice` with structured error responses
- 🗂️ **DTO Layer** — Clean separation between database entities and API responses via mappers
- ✅ **50 Automated Tests** — Unit tests (Mockito) + Web layer integration tests (MockMvc)
- 🐳 **Docker Ready** — Multi-stage Dockerfile + Docker Compose with PostgreSQL orchestration
- 🔒 **Secrets Management** — Environment variables via `.env` file, never hardcoded
- 📖 **Swagger UI** — Interactive API documentation with JWT authorization support

---

## 🖥️ Full Stack Version

This repository contains the **backend API only**.

A complete frontend is available and fully integrated with this API:

👉 **[TaskManager Frontend](https://github.com/alshwadfy/TaskManager)**

### To run the full stack:
1. Clone and run this backend first
2. Clone the frontend repo and follow its README

---

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended)

```bash
git clone https://github.com/alshwadfy/task-manager-api
cd task-manager-api
cp .env.example .env
docker compose up --build
```

App is live at: `http://localhost:8080`

---

### Option 2: Run Locally

**Prerequisites:** Java 21+, Maven, PostgreSQL running on port 5432

```bash
git clone https://github.com/alshwadfy/task-manager-api
./mvnw spring-boot:run
```

---

## 📖 API Documentation (Swagger UI)

**`http://localhost:8080/swagger-ui.html`**

1. **Register** via `POST /api/auth/register` (receives verification email)
2. **Verify Email** via `POST /api/auth/verify`
3. **Login** via `POST /api/auth/login` and copy your JWT token
4. Click 🔓 **Authorize** and paste `Bearer <your-token>`
5. Test all secured endpoints directly!

---

## 🔗 API Endpoints

### 🔐 Auth
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user & trigger verification email | Public |
| `POST` | `/api/auth/verify` | Verify email with 6-digit code | Public |
| `POST` | `/api/auth/resend-verification` | Resend verification email code | Public |
| `POST` | `/api/auth/login` | Login & receive JWT token (verified accounts) | Public |

### 📁 Task Lists
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/task-lists?page=0&size=10` | Get paginated task lists | 🔐 JWT |
| `POST` | `/api/task-lists` | Create a new task list | 🔐 JWT |
| `GET` | `/api/task-lists/{id}` | Get a task list by ID | 🔐 JWT |
| `PUT` | `/api/task-lists/{id}` | Update a task list | 🔐 JWT |
| `DELETE` | `/api/task-lists/{id}` | Delete a task list | 🔐 JWT |

### ✅ Tasks
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/task-lists/{listId}/tasks?page=0&size=10` | Get paginated tasks | 🔐 JWT |
| `POST` | `/api/task-lists/{listId}/tasks` | Create a task | 🔐 JWT |
| `GET` | `/api/task-lists/{listId}/tasks/{taskId}` | Get a task by ID | 🔐 JWT |
| `PUT` | `/api/task-lists/{listId}/tasks/{taskId}` | Update a task | 🔐 JWT |
| `DELETE` | `/api/task-lists/{listId}/tasks/{taskId}` | Delete a task | 🔐 JWT |

---

## 🧪 Running Tests

```bash
./mvnw test
```

**50 tests across 6 test suites:**
- `AuthControllerTest` — 8 MockMvc tests
- `TaskListControllerTest` — 7 MockMvc tests
- `TasksControllerTest` — 6 MockMvc tests
- `UserServiceImplTest` — 19 Mockito unit tests
- `VerificationTokenServiceImplTest` — 7 Mockito unit tests
- `EmailServiceImplTest` — 2 Mockito unit tests
- `TasksApplicationTests` — 1 context / smoke test

---

## 🗂️ Project Structure

```
src/
├── main/java/com/projects/Tasks/
│   ├── config/
│   ├── Controller/
│   ├── domain/
│   │   ├── dto/
│   │   └── entities/
│   ├── exceptions/
│   ├── mappers/
│   ├── repositories/
│   ├── security/
│   └── services/
└── test/java/com/projects/Tasks/
    ├── Controller/
    └── Services/
```

---

## 🐳 Docker Architecture

```
┌─────────────────────────────────┐
│         docker compose          │
│  ┌──────────────────────────┐   │
│  │  app (Spring Boot)       │◄──┼── REST Requests
│  │  Multi-stage Docker Build │  │
│  └──────────┬───────────────┘   │
│             ▼ (healthcheck)     │
│  ┌──────────────────────────┐   │
│  │  db (PostgreSQL 16)      │   │
│  │  Volume: postgres_data   │   │
│  └──────────────────────────┘   │
└─────────────────────────────────┘
```

Multi-stage build: **~800MB → ~180MB**

---

## 🔒 Security Configuration

```bash
cp .env.example .env

POSTGRES_DB=taskmanager
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-specific-password
```

---

## 📌 Environment Variables

| Variable | Description |
| :--- | :--- |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Schema strategy |
| `JWT_SECRET` | Secret key for JWT signing |
| `MAIL_USERNAME` | SMTP username / sender email |
| `MAIL_PASSWORD` | SMTP password / app password |
