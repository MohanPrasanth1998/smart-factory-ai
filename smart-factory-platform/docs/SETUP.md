# Smart Factory Platform — local setup

## Prerequisites

- Docker Desktop / Docker Engine with Compose v2
- Java 21 for local Maven builds
- `curl` (or Postman) for API checks

## Start infrastructure and services

```bash
docker compose config
docker compose up --build -d
docker compose ps
```

Watch a service: `docker compose logs -f telemetry-service`. Stop without deleting data: `docker compose down`. The development stack uses MySQL 8.4, PostgreSQL 16 for telemetry, Redis 7.4 and Kafka 3.9.1.

## Local service ports

| Service | Port | Health endpoint |
|---|---:|---|
| API Gateway | 9090 | `/actuator/health` |
| Factory | 7070 | `/actuator/health` |
| Auth | 7080 | `/actuator/health` |
| Machine | 7072 | `/actuator/health` |
| Telemetry | 7073 | `/actuator/health` |
| Device Simulator | 7074 | `/actuator/health` and `/api/v1/simulator/health` |
| Maintenance | 7075 | `/actuator/health` |
| Production | 7076 | `/actuator/health` |
| Analytics | 7077 (container port 8080) | `/actuator/health` |
| Notification | 7078 (container port 8080) | `/actuator/health` |
| Kafka | 9092 | broker API |
| Redis | 6379 | `redis-cli ping` |

## First-time environment configuration

Copy `.env.example` to `.env` and replace the sample passwords. The `.env` file is ignored by Git. Compose creates a development RSA keypair in a named Docker volume for local JWT signing/verification; the private key is not stored in the source tree. Existing database volumes are not automatically reinitialized with new SQL scripts.

## Device simulator

Emit one sample reading:

```bash
curl -X POST http://localhost:7074/api/v1/simulator/devices/MACHINE-001/telemetry
```

Emit 20 readings, one second apart:

```bash
curl -X POST http://localhost:7074/api/v1/simulator/run -H "Content-Type: application/json" -d '{"machineCode":"MACHINE-001","count":20,"intervalMs":1000}'
```

Stop a batch: `curl -X POST http://localhost:7074/api/v1/simulator/stop`. Register the factory and machine first; the telemetry consumer validates `machineCode` against Machine Service before persisting simulator readings.

## Build each service

From each service folder (Windows PowerShell use `mvnw.cmd`; macOS/Linux use `./mvnw`):

```bash
./mvnw clean verify
```

A project-level script `scripts/verify-services.sh` attempts a clean verify for each Maven module and writes logs to `build-logs/`. Build checks require Maven wrapper downloads and access to configured artifact repositories.

## Additional service APIs

- Maintenance: `POST /api/v1/maintenance`, `GET /api/v1/maintenance`, `GET /api/v1/maintenance/{id}`, `PATCH /api/v1/maintenance/{id}/status`.
- Production orders: `POST /api/v1/production/orders`, `GET /api/v1/production/orders`, `GET /api/v1/production/orders/{id}`, `PATCH /api/v1/production/orders/{id}/progress`.

Example create maintenance request:

```json
{"machineCode":"MACHINE-001","description":"Inspect vibration warning","scheduledAt":"2026-10-22T09:00:00Z"}
```

Example create production order:

```json
{"machineCode":"MACHINE-001","productCode":"PART-100","plannedQuantity":500}
```

## Security notes

- Rotate all sample passwords before any shared or production environment.
- Do not commit `.env`, private JWT keys, certificates, cloud credentials, or production secrets.
- Development Compose currently uses local credentials for convenience only.
## Develop and QA profiles

See [PROFILES_DEVELOP_QA.md](PROFILES_DEVELOP_QA.md) for profile selection, environment variables, and the QA Compose override.
