# Develop and QA profiles

Every non-AI Spring Boot service has `application-develop.yaml` and `application-qa.yaml` beside its base `application.yaml`. The AI service is intentionally unchanged, per the project requirement to skip AI-related work.

## Profile intent

- **develop**: runs services from the developer workstation; uses `localhost` defaults for MySQL (`3306`), PostgreSQL (`15432`), Kafka (`9092`) and Redis (`6379`). Use environment variables to override passwords/URLs. JPA schema mode remains `validate`; migrations remain responsible for schema changes.
- **qa**: runs on a Docker Compose network; uses service DNS names (`mysql`, `postgres-telemetry`, `kafka`, `redis`, and other service names). Database credentials have no fallback in the profile and must be supplied externally. JPA remains `validate`; do not enable `ddl-auto=update` in QA.

## Run a single service locally (develop)

From the service directory, for example `services/factory`:

```powershell
$env:SPRING_PROFILES_ACTIVE = "develop"
$env:SPRING_DATASOURCE_PASSWORD = "your-local-password"
./mvnw.cmd spring-boot:run
```

On macOS/Linux, use `./mvnw spring-boot:run`. Ensure the local MySQL/PostgreSQL database, Kafka, and Redis endpoints are running. The exact datasource URL can be overridden with `SPRING_DATASOURCE_URL`.

## Run the Compose stack with QA profile

1. Copy `.env.qa.example` to `.env` and set strong local QA-only values.
2. Start the stack:

```powershell
docker compose -f docker-compose.yml -f docker-compose.qa.yml config
docker compose -f docker-compose.yml -f docker-compose.qa.yml up --build -d
```

3. Inspect status and logs:

```powershell
docker compose -f docker-compose.yml -f docker-compose.qa.yml ps
docker compose -f docker-compose.yml -f docker-compose.qa.yml logs --tail=100 factory-service
```

The override activates `qa` for the application services and points their infrastructure clients at the Compose service DNS names. It does not run Git commands.

## Environment variables commonly used

- `SPRING_PROFILES_ACTIVE=develop|qa`
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`
- `SPRING_DATA_REDIS_HOST`, `SPRING_DATA_REDIS_PORT`
- `CLIENTS_FACTORY_URL`, `CLIENTS_MACHINE_URL`, `AUTH_SERVICE_URL`, `FACTORY_SERVICE_URL`
- `TELEMETRY_INGEST_TOPIC`, `TELEMETRY_TOPIC`

## Notes and safety

- Profiles are configuration overlays; they do not create database instances or guarantee the service starts without its dependencies.
- QA should use isolated databases and secrets managed by the deployment environment. Do not reuse production credentials or commit `.env` files.
- `application-qa.yaml` intentionally requires database credentials via environment variables when a service uses a database. Missing values should be treated as a configuration error, not replaced with a shared default password.
- AI service configuration and AI features were not changed.
