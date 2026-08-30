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
- 📄 **Pagination & Sorting** — Spring Data `Pageable` with full page metadata (`totalPages`, `totalElements`)
- 🛡️ **Global Exception Handling** — Centralized `@ControllerAdvice` with structured error responses
- 🗂️ **DTO Layer** — Clean separation between database entities and API responses via mappers
- ✅ **32 Automated Tests** — Unit tests (Mockito) + Web layer integration tests (MockMvc)
- 🐳 **Docker Ready** — Multi-stage Dockerfile + Docker Compose with PostgreSQL orchestration
- 🔒 **Secrets Management** — Environment variables via `.env` file, never hardcoded
- 📖 **Swagger UI** — Interactive API documentation with JWT authorization support

---

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended)

```bash
# 1. Clone the repository
git clone <https://github.com/alshwadfy/TaskManager>
cd "project 2/Tasks"

# 2. Create your environment variables file
cp .env.example .env
# Edit .env with your desired passwords

# 3. Start the full stack (PostgreSQL + Spring Boot)
docker compose up --build
```

App is live at: `http://localhost:8080`

---

### Option 2: Run Locally

**Prerequisites:** Java 21+, Maven, PostgreSQL running on port 5432

```bash
# Clone and navigate
cd "project 2/Tasks"

# Set up your database (update application.properties if needed)
# Then run:
./mvnw spring-boot:run
```

---

## 📖 API Documentation (Swagger UI)

Once the app is running, open your browser:

**`http://localhost:8080/swagger-ui.html`**

The interactive UI lets you:
1. **Register** a new user via `POST /api/auth/register`
2. **Login** via `POST /api/auth/login` and copy your JWT token
3. Click the 🔓 **Authorize** button and paste `Bearer <your-token>`
4. Test all secured endpoints directly from the browser!

---

## 🔗 API Endpoints

### 🔐 Auth
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user | Public |
| `POST` | `/api/auth/login` | Login & receive JWT token | Public |

### 📁 Task Lists
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/task-lists?userId={id}&page=0&size=10` | Get paginated task lists for a user | 🔐 JWT |
| `POST` | `/api/task-lists?userId={id}` | Create a new task list | 🔐 JWT |
| `GET` | `/api/task-lists/{id}` | Get a task list by ID | 🔐 JWT |
| `PUT` | `/api/task-lists/{id}` | Update a task list | 🔐 JWT |
| `DELETE` | `/api/task-lists/{id}` | Delete a task list | 🔐 JWT |

### ✅ Tasks
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/task-lists/{listId}/tasks?page=0&size=10` | Get paginated tasks in a list | 🔐 JWT |
| `POST` | `/api/task-lists/{listId}/tasks` | Create a task | 🔐 JWT |
| `GET` | `/api/task-lists/{listId}/tasks/{taskId}` | Get a task by ID | 🔐 JWT |
| `PUT` | `/api/task-lists/{listId}/tasks/{taskId}` | Update a task | 🔐 JWT |
| `DELETE` | `/api/task-lists/{listId}/tasks/{taskId}` | Delete a task | 🔐 JWT |

---

## 🧪 Running Tests

```bash
./mvnw test
```

**32 tests across 4 test suites:**
- `AuthControllerTest` — 3 MockMvc web layer tests
- `TaskListControllerTest` — 7 MockMvc web layer tests
- `TasksControllerTest` — 6 MockMvc web layer tests
- `UserServiceImplTest` — 15 Mockito unit tests

---

## 🗂️ Project Structure

```
src/
├── main/java/com/projects/Tasks/
│   ├── config/                  # SecurityConfig, OpenApiConfig
│   ├── Controller/              # REST Controllers + GlobalExceptionHandler
│   ├── domain/
│   │   ├── dto/                 # Request/Response DTOs
│   │   └── entities/            # JPA Entities
│   ├── exceptions/              # Custom Exceptions
│   ├── mappers/                 # Entity ↔ DTO Mappers
│   ├── repositories/            # Spring Data JPA Repositories
│   ├── security/                # JwtService, JwtAuthFilter
│   └── services/                # Service Interfaces & Implementations
└── test/java/com/projects/Tasks/
    ├── Controller/              # MockMvc Integration Tests
    └── Services/                # Mockito Unit Tests
```

---

## 🐳 Docker Architecture

```
┌─────────────────────────────────┐
│         docker compose          │
│                                 │
│  ┌──────────────────────────┐   │
│  │  app (Spring Boot)       │   │
│  │  Port: 8080              │◄──┼── REST Requests
│  │  Multi-stage Docker Build │  │
│  └──────────┬───────────────┘   │
│             │ depends_on        │
│             ▼ (healthcheck)     │
│  ┌──────────────────────────┐   │
│  │  db (PostgreSQL 16)      │   │
│  │  Port: 5432              │   │
│  │  Volume: postgres_data   │   │
│  └──────────────────────────┘   │
└─────────────────────────────────┘
```

Multi-stage build reduces image size from **~800MB → ~180MB** by using `eclipse-temurin:21-jre-alpine` at runtime.

---

## 🔒 Security Configuration

All secrets are externalized via `.env` (never committed to Git):

```bash
# Copy the template
cp .env.example .env

# Fill in your values in .env:
POSTGRES_DB=taskmanager
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key
```

---

## 📌 Environment Variables Reference

| Variable | Description | Default (local) |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `password` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Schema strategy | `update` |
