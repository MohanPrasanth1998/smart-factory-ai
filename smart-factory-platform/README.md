# Smart Factory Platform

A production-oriented, event-driven **Smart Factory Management and AI Platform** built using Java, Spring Boot, Spring Cloud, Kafka, MQTT, Redis, and Generative AI.

The platform is designed to manage factories and machines, collect real-time machine telemetry, detect operational issues, generate alerts, and eventually provide an AI-powered assistant for factory operations.

---

## 1. Project Goals

The platform aims to provide:

* Factory management
* Machine management
* Machine telemetry ingestion
* Real-time machine monitoring
* Machine health and status tracking
* Maintenance management
* Event-driven communication
* Alert generation
* Real-time dashboard updates
* Analytics and anomaly detection
* Predictive maintenance
* AI-powered factory assistant
* RAG-based knowledge retrieval
* AI tool calling
* AI agents

The project is being developed incrementally using production-oriented backend engineering practices.

---

## 2. Technology Stack

| Technology      | Purpose                         |
| --------------- | ------------------------------- |
| Java 21         | Programming language            |
| Spring Boot     | Application framework           |
| Spring Cloud    | Microservices infrastructure    |
| Spring Data JPA | Database access                 |
| MySQL           | Relational database             |
| Flyway          | Database migrations             |
| Redis           | Caching and fast-access data    |
| Apache Kafka    | Event-driven communication      |
| MQTT            | Machine/IoT communication       |
| WebSocket       | Real-time dashboard updates     |
| LangChain4j     | AI integration                  |
| LLM             | Generative AI                   |
| Vector Database | Semantic search / RAG           |
| Docker          | Containerization                |
| Kubernetes      | Container orchestration         |
| Actuator        | Application monitoring          |
| Maven           | Build and dependency management |

---

## 3. High-Level Architecture

```text
                         ┌──────────────────┐
                         │      Client      │
                         │ Web / Dashboard  │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   API Gateway    │
                         └────────┬─────────┘
                                  │
             ┌────────────────────┼────────────────────┐
             │                    │                    │
             ▼                    ▼                    ▼
      ┌─────────────┐      ┌─────────────┐      ┌─────────────┐
      │   Factory   │      │   Machine   │      │ Maintenance │
      │   Service   │      │   Service   │      │   Service   │
      └──────┬──────┘      └──────┬──────┘      └─────────────┘
             │                    │
             ▼                    ▼
          MySQL                MySQL


Machines / IoT Devices
          │
          │ MQTT
          ▼
   ┌───────────────┐
   │ MQTT Broker   │
   └───────┬───────┘
           │
           ▼
   ┌───────────────┐
   │ IoT Ingestion │
   │    Service    │
   └───────┬───────┘
           │
           │ Kafka Events
           ▼
   ┌─────────────────────────────────────┐
   │             Kafka                   │
   │ telemetry / alerts / maintenance    │
   └───────┬───────────────┬─────────────┘
           │               │
           ▼               ▼
      ┌──────────┐    ┌──────────────┐
      │Analytics │    │ Notification │
      │ Service  │    │   Service    │
      └──────────┘    └──────────────┘


                    ┌─────────────────┐
                    │    AI Service   │
                    ├─────────────────┤
                    │ LangChain4j     │
                    │ RAG             │
                    │ Vector DB       │
                    │ Tool Calling    │
                    │ AI Agent        │
                    └─────────────────┘
```

---

## 4. Microservices

The platform will be developed incrementally.

### Current

#### Factory Service

Responsible for:

* Factory creation
* Factory information
* Factory status
* Factory retrieval
* Factory updates
* Factory deletion

### Planned

#### Machine Service

Responsible for:

* Machine registration
* Machine configuration
* Machine status
* Machine health

#### IoT Ingestion Service

Responsible for:

* MQTT communication
* Machine telemetry ingestion
* Converting IoT messages into application events

#### Maintenance Service

Responsible for:

* Maintenance records
* Maintenance schedules
* Maintenance history
* Maintenance status

#### Alert Service

Responsible for:

* Threshold-based alerts
* Machine failure alerts
* Anomaly alerts
* Alert lifecycle

#### Analytics Service

Responsible for:

* Machine metrics
* Factory metrics
* Historical analysis
* Anomaly detection
* Predictive maintenance data

#### Notification Service

Responsible for:

* Email notifications
* Application notifications
* Alert delivery

#### AI Service

Responsible for:

* Generative AI
* Factory knowledge retrieval
* RAG
* Tool calling
* AI agents
* Natural-language factory queries

---

## 5. Communication Strategy

The platform will use different communication mechanisms depending on the requirement.

### REST / OpenFeign

Used for synchronous service-to-service operations where an immediate response is required.

Example:

```text
Machine Service
      │
      │ OpenFeign
      ▼
Factory Service
```

### Apache Kafka

Used for asynchronous and event-driven communication.

Example:

```text
IoT Service
    │
    │ MachineTelemetryEvent
    ▼
  Kafka
    │
    ├──► Alert Service
    ├──► Analytics Service
    └──► Notification Service
```

### MQTT

Used for communication between machines/IoT devices and the platform.

```text
Machine
   │
   │ MQTT
   ▼
MQTT Broker
   │
   ▼
IoT Ingestion Service
```

### WebSocket

Used for pushing real-time updates to dashboards.

```text
Machine Event
     │
     ▼
Backend
     │
     │ WebSocket
     ▼
Dashboard
```

---

## 6. Factory Service

The first service being implemented is the Factory Service.

### Current technology

* Spring Boot
* Spring MVC
* Spring Data JPA
* MySQL
* Flyway
* Bean Validation
* Actuator
* Lombok

### Package Structure

```text
factory-service
└── src
    └── main
        ├── java
        │   └── com.smartfactory.factory
        │       ├── controller
        │       ├── dto
        │       ├── entity
        │       ├── repository
        │       └── service
        │
        └── resources
            ├── application.yaml
            └── db
                └── migration
```

---

## 7. Development Approach

Each feature follows this workflow:

```text
Requirement
    ↓
Domain Design
    ↓
Database Design
    ↓
API Contract
    ↓
Implementation
    ↓
Validation
    ↓
Error Handling
    ↓
Testing
    ↓
Documentation
    ↓
Git Commit
```

This keeps the project maintainable as the number of microservices increases.

---

## 8. Database Migration Strategy

Flyway is responsible for database schema changes.

Hibernate is configured to validate the schema rather than create or modify it.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true
```

Migration files are stored under:

```text
src/main/resources/db/migration/
```

Example:

```text
V1__create_factories_table.sql
V2__add_factory_indexes.sql
V3__...
```

The Flyway schema history table tracks which migrations have already been applied.

---

## 9. Validation Strategy

API requests use Jakarta Bean Validation.

Example:

```java
@NotBlank
@Size(max = 100)
String name;
```

Invalid requests should be rejected before reaching the service/database layer.

---

## 10. API Versioning

REST APIs use URL-based versioning.

Example:

```text
/api/v1/factories
```

Future breaking API changes can use:

```text
/api/v2/factories
```

---

## 11. Testing Strategy

The project will contain multiple levels of testing.

### Unit Tests

Test individual classes in isolation.

Example:

```text
FactoryServiceTest
```

### Integration Tests

Test multiple Spring components together.

Example:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

### API Tests

Test HTTP contracts, validation, and error responses.

### Eventually

* Testcontainers
* Kafka integration tests
* MQTT integration tests
* Contract testing
* End-to-end tests

---

## 12. Observability

The platform will eventually include:

* Spring Boot Actuator
* Structured logging
* Correlation IDs
* Metrics
* Distributed tracing
* Health checks
* Readiness/liveness probes

The objective is to make failures traceable across multiple microservices.

---

## 13. Security

Security will be introduced as the platform grows.

Planned capabilities include:

* Spring Security
* Authentication
* Authorization
* JWT
* Role-based access control
* API Gateway security
* Service-to-service security
* Secret management

---

## 14. AI Roadmap

The AI layer will be introduced after the core backend platform is stable.

Planned progression:

```text
LLM
 ↓
Spring AI / LangChain4j
 ↓
Prompt Engineering
 ↓
Embeddings
 ↓
Vector Database
 ↓
RAG
 ↓
Tool Calling
 ↓
AI Agent
 ↓
Factory AI Assistant
```

Example future query:

```text
"Which machines have shown abnormal temperature
readings during the last 24 hours?"
```

The AI service should be able to retrieve the relevant data through tools and provide a meaningful response.

---

## 15. Production Roadmap

### Phase 1 — Core Backend

* [x] Project setup
* [x] Factory service setup
* [x] MySQL setup
* [x] Flyway setup
* [x] Factory entity
* [x] Factory repository
* [x] Factory DTOs
* [x] Factory service
* [x] Factory controller

### Phase 2 — CRUD and Quality

* [ ] Factory GET APIs
* [ ] Factory UPDATE API
* [ ] Factory DELETE API
* [ ] Global exception handling
* [ ] Error response model
* [ ] Unit tests
* [ ] Integration tests
* [ ] API documentation

### Phase 3 — Microservices

* [ ] Machine Service
* [ ] Maintenance Service
* [ ] API Gateway
* [ ] Service discovery/configuration where required
* [ ] OpenFeign

### Phase 4 — Event Driven Architecture

* [ ] Kafka
* [ ] Producers
* [ ] Consumers
* [ ] Consumer groups
* [ ] Retry handling
* [ ] Dead-letter topics
* [ ] Event schemas

### Phase 5 — IoT

* [ ] MQTT broker
* [ ] MQTT ingestion
* [ ] Telemetry events
* [ ] Machine heartbeat
* [ ] Real-time machine status

### Phase 6 — Real-Time Platform

* [ ] WebSocket
* [ ] Live machine dashboard
* [ ] Real-time alerts

### Phase 7 — Analytics

* [ ] Machine metrics
* [ ] Anomaly detection
* [ ] Historical analysis
* [ ] Predictive maintenance

### Phase 8 — Generative AI

* [ ] LLM integration
* [ ] Prompt engineering
* [ ] Embeddings
* [ ] Vector database
* [ ] RAG
* [ ] Tool calling
* [ ] AI agent
* [ ] Factory AI assistant

### Phase 9 — Production

* [ ] Docker
* [ ] Kubernetes
* [ ] Config/secrets management
* [ ] Observability
* [ ] Distributed tracing
* [ ] CI/CD
* [ ] Production deployment

---

## 16. Local Development

### Requirements

```text
Java 21
Maven
MySQL
Git
```

Additional infrastructure such as Kafka, Redis, MQTT, and Kubernetes will be introduced as their corresponding development phases begin.

### Build

```bash
./mvnw clean test
```

### Run

```bash
./mvnw spring-boot:run
```

---

## 17. Project Status

The repository includes core factory, machine, auth, gateway and telemetry services, plus initial maintenance and production APIs, Kafka consumers, Redis event caching, and a device simulator. See `docs/IMPLEMENTATION_NOTES.md` for implementation boundaries and verification status.

Factory CRUD flow:

```text
Client → API Gateway → FactoryController → FactoryService → FactoryRepository → MySQL → DTO response
```

---

## 18. Engineering Principles

The project follows these principles:

* Keep controllers thin
* Keep business logic in services
* Keep persistence logic in repositories
* Do not expose JPA entities directly through APIs
* Use DTOs for API contracts
* Use Flyway for schema migrations
* Keep Hibernate schema validation enabled
* Use enums safely
* Validate API input
* Use transactions for database operations
* Prefer synchronous communication only when required
* Prefer events for asynchronous workflows
* Design for observability
* Test important business behavior
* Document architectural decisions
* Introduce complexity only when the requirement needs it


## Current backend baseline (non-AI)

The repository now includes a Docker Compose development stack for MySQL, PostgreSQL, Redis, Kafka and the backend services, plus a Device Simulator Service for emitting sample machine telemetry. AI service code is intentionally not expanded as requested.

### Quick start

See [docs/SETUP.md](docs/SETUP.md) for prerequisites, ports, commands and simulator examples. Architecture and event-flow diagrams are in [docs/architecture/EVENT_DRIVEN_WORKFLOW.md](docs/architecture/EVENT_DRIVEN_WORKFLOW.md) and [docs/architecture/SEQUENCE_DIAGRAMS.md](docs/architecture/SEQUENCE_DIAGRAMS.md).

### Device simulator endpoints

- `POST /api/v1/simulator/devices/{machineCode}/telemetry` — emit one reading to Kafka (`factory.telemetry.ingest.v1`).
- `POST /api/v1/simulator/run` — start a bounded batch with `machineCode`, `count`, and `intervalMs`.
- `POST /api/v1/simulator/stop` — request stop for the active batch.
- `GET /api/v1/simulator/health` — simulator state.

### Verification status

The Compose file and Maven modules should be validated in the target developer environment. The assistant working environment may not have Docker available and Maven builds require the configured Maven repositories; do not treat source generation alone as proof that every container starts. No Git commit or push is part of this task.

### Maintenance and production APIs

- Maintenance records: `POST/GET /api/v1/maintenance`, `GET /api/v1/maintenance/{id}`, `PATCH /api/v1/maintenance/{id}/status`.
- Production orders: `POST/GET /api/v1/production/orders`, `GET /api/v1/production/orders/{id}`, `PATCH /api/v1/production/orders/{id}/progress`.

These APIs have Flyway-managed MySQL schemas and basic request validation. See `docs/SETUP.md` for example payloads.
## Environment profiles

See [Develop and QA profiles](docs/PROFILES_DEVELOP_QA.md) for per-service `develop`/`qa` configuration and run commands.

## API testing

Import the Postman collection and environment files from [`postman/`](postman/) and follow [`docs/API_COLLECTION_AND_VERIFICATION.md`](docs/API_COLLECTION_AND_VERIFICATION.md).

## CI/CD

See [`jenkins/README.md`](jenkins/README.md) and [`docs/CICD_JENKINS_BLUE_OCEAN_RUNDECK.md`](docs/CICD_JENKINS_BLUE_OCEAN_RUNDECK.md) for the Jenkins + Blue Ocean CI/CD setup, GHCR publishing, local Windows Docker deployment, remote QA deployment, and Rundeck-style operations.

## Jenkins CI/CD (Blue Ocean) and Dev/QA deployment

The primary CI/CD configuration is the root `Jenkinsfile`. See [`jenkins/README.md`](jenkins/README.md) and [`docs/CICD_JENKINS_BLUE_OCEAN_RUNDECK.md`](docs/CICD_JENKINS_BLUE_OCEAN_RUNDECK.md) for agent prerequisites, Jenkins credentials, GHCR publishing, local Windows Docker deployment, remote QA deployment, and Rundeck-style operational jobs. GitHub Actions workflows are not included in this Jenkins-focused package.
