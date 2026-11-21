# MHT-CET Microservices Backend Platform

An enterprise-grade, distributed microservices platform designed for conducting and scoring **MHT-CET (Maharashtra Common Entrance Test)** online examinations. Built with **Java 21**, **Spring Boot**, **Spring Cloud**, **OpenFeign**, **Spring Security with JWT**, and **Docker Compose**.

---

## Architecture Overview

```text
                                  [ Client / Postman / Frontend ]
                                                 │
                                                 ▼
                                     ┌───────────────────────┐
                                     │  API Gateway (8080)   │ (JWT Auth Filter)
                                     └───────────┬───────────┘
                                                 │
                 ┌───────────────────────────────┼───────────────────────────────┐
                 │                               │                               │
                 ▼                               ▼                               ▼
       ┌──────────────────┐            ┌──────────────────┐            ┌──────────────────┐
       │ Identity Service │            │  Question Bank   │            │   Test Engine    │
       │     (8084)       │            │  Service (8081)  │            │  Service (8082)  │
       └──────────────────┘            └─────────┬────────┘            └─────────┬────────┘
                                                 │                               │
                                                 └───────────────┬───────────────┘
                                                                 │ (OpenFeign)
                                                                 ▼
                                                       ┌──────────────────┐
                                                       │    Evaluation    │
                                                       │  Service (8083)  │
                                                       └──────────────────┘

                                    ┌────────────────────────┐
                                    │ Eureka Server (8761)   │ (Service Registry)
                                    └────────────────────────┘
```

---

## Microservices Breakdown

| Service Name | Port | Description |
| :--- | :--- | :--- |
| **`eureka-server`** | `8761` | Central Service Discovery Registry. Dynamically tracks all live instances. |
| **`api-gateway`** | `8080` | Single entry point for clients. Validates JWT tokens and routes requests via Eureka. |
| **`identity-service`** | `8084` | Manages user registration, BCrypt password hashing, and issues signed JWT tokens. |
| **`question-bank-service`** | `8081` | Question repository for Physics, Chemistry, Mathematics, and Biology subjects. |
| **`test-engine-service`** | `8082` | Manages test attempts, 90-minute sectional locks, answer auto-saving, and exam submissions. |
| **`evaluation-service`** | `8083` | Computes exam scores using MHT-CET rules (+2 Maths, +1 Phy/Chem/Bio, 0 negative marks). |

---

## Core Features

- **Centralized Security at API Gateway:** Stateless authentication using JWT tokens verified at the edge (`api-gateway`). trusted user details (`X-User-Id`, `X-User-Role`) are safely injected downstream.
- **Dynamic Service Discovery:** Zero hardcoded IP addresses or ports using Netflix Eureka. Microservices resolve each other by name.
- **Microservice Orchestration via OpenFeign:** `evaluation-service` dynamically aggregates data from `test-engine-service` (student options) and `question-bank-service` (official answer keys).
- **Stateful Sectional Lock Enforcement:** Implements the official MHT-CET exam format—Section 1 (Physics & Chemistry) locks permanently after 90 minutes or on transition before Section 2 (Mathematics or Biology) unlocks.
- **Idempotent Evaluation Engine:** Ensures repeated evaluation calls for the same `attemptId` return the cached scorecard without recalculation or redundant DB writes.
- **Containerized Deployment:** Multi-stage `Dockerfile` configurations optimized for minimal image footprints and instant multi-container deployment via Docker Compose.

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.x / 4.2.x, Spring Data JPA, Spring Security
- **Cloud Infrastructure:** Spring Cloud Gateway, Netflix Eureka Server/Client, OpenFeign
- **Authentication:** JSON Web Tokens (JJWT 0.12.6) & BCrypt
- **Database:** H2 In-Memory Database (per-service isolation pattern)
- **Containerization:** Docker, Docker Compose
- **Build Tool:** Apache Maven

---

## Project Directory Structure

```text
mhtcet-backend/
├── docker-compose.yml
├── eureka-server/
│   ├── Dockerfile
│   └── pom.xml
├── api-gateway/
│   ├── Dockerfile
│   └── pom.xml
├── identity-service/
│   ├── Dockerfile
│   └── pom.xml
├── question-bank-service/
│   ├── Dockerfile
│   └── pom.xml
├── test-engine-service/
│   ├── Dockerfile
│   └── pom.xml
└── evaluation-service/
    ├── Dockerfile
    └── pom.xml
```

---

## Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.
- Java 21 JDK (if running/building outside Docker).
- Maven 3.9+ (optional, Maven wrapper or Docker multi-stage builds can be used).

---

### Method 1: Running with Docker Compose (Recommended)

1. Clone or organize all 6 microservices into a single parent folder (e.g., `mhtcet-backend`).
2. Open PowerShell / Terminal in the parent directory and build/launch all containers:
   ```bash
   docker compose up --build -d
   ```
3. Check status of all containers:
   ```bash
   docker compose ps
   ```
4. Verify Eureka Registry by opening [http://localhost:8761](http://localhost:8761) in your browser. All 5 client services should appear under **Instances currently registered with Eureka**.
5. Stop all containers cleanly when finished:
   ```bash
   docker compose down
   ```

---

### Method 2: Running Locally via IntelliJ IDEA

Start the applications in your IDE in the following order:
1. `eureka-server` (`8761`)
2. `identity-service` (`8084`)
3. `question-bank-service` (`8081`)
4. `test-engine-service` (`8082`)
5. `evaluation-service` (`8083`)
6. `api-gateway` (`8080`)

---

## API Testing Workflow (Via Gateway Port 8080)

### 1. Register a New Student
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "student@mhtcet.ac.in",
  "password": "Password@123",
  "role": "STUDENT"
}
```

### 2. Login & Obtain JWT Token
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "student@mhtcet.ac.in",
  "password": "Password@123"
}
```
*Response returns `{ "token": "<JWT_TOKEN>", "userId": "...", "role": "STUDENT" }`.*

---

### 3. Seed Questions (Question Bank Service)
```http
POST http://localhost:8080/api/questions
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "subject": "MATHEMATICS",
  "standard": "XII",
  "chapter": "Matrices",
  "questionText": "If A is a 3x3 matrix and |A| = 4, find |2A|.",
  "options": [
    {"optionId": "A", "text": "8"},
    {"optionId": "B", "text": "32"},
    {"optionId": "C", "text": "16"},
    {"optionId": "D", "text": "64"}
  ],
  "correctOptionId": "B",
  "explanation": "|kA| = k^n |A| = 2^3 * 4 = 32."
}
```

---

### 4. Start Exam Attempt (Test Engine Service)
```http
POST http://localhost:8080/api/attempts/start?userId=student123&stream=PCM
Authorization: Bearer <JWT_TOKEN>
```
*Note the returned `attemptId` UUID for subsequent calls.*

---

### 5. Auto-Save Student Answer
```http
PUT http://localhost:8080/api/attempts/{attemptId}/answer?questionId=1&option=B
Authorization: Bearer <JWT_TOKEN>
```

---

### 6. Transition to Section 2 (Maths/Biology)
```http
POST http://localhost:8080/api/attempts/{attemptId}/next-section
Authorization: Bearer <JWT_TOKEN>
```

---

### 7. Submit Exam
```http
POST http://localhost:8080/api/attempts/{attemptId}/submit
Authorization: Bearer <JWT_TOKEN>
```

---

### 8. Grade Exam (Evaluation Service)
```http
POST http://localhost:8080/api/evaluations/{attemptId}
Authorization: Bearer <JWT_TOKEN>
```
*Returns calculated scorecard with total marks, accuracy percentage, and breakdown.*

---

## H2 Database Consoles

Each service maintains its own isolated database instance:

| Service | Console URL | JDBC URL |
| :--- | :--- | :--- |
| **Identity Service** | `http://localhost:8084/h2-console` | `jdbc:h2:mem:mhtcet_identity` |
| **Question Bank** | `http://localhost:8081/h2-console` | `jdbc:h2:mem:mhtcet_qbank` |
| **Test Engine** | `http://localhost:8082/h2-console` | `jdbc:h2:mem:mhtcet_test_engine` |
| **Evaluation Service** | `http://localhost:8083/h2-console` | `jdbc:h2:mem:mhtcet_evaluation` |

*Username:* `sa`  
*Password:* *(leave blank)*