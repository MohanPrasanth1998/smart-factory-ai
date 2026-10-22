# Implementation notes and verification boundaries

## Implemented in this update

- Added a root `docker-compose.yml` for MySQL 8.4, PostgreSQL 16, Redis 7.4, single-node Kafka 3.9.1, and the non-AI backend services.
- Added an ephemeral local RSA key-init container. Its keypair is stored in a named Docker volume rather than in source control; this is for local development only.
- Added a Device Simulator Service with one-shot telemetry, bounded batch generation, stop, and health endpoints.
- Added separate Kafka topics to prevent loops: `factory.telemetry.ingest.v1` is raw simulator/device input; `factory.telemetry.v1` is emitted after telemetry persistence.
- Added a telemetry Kafka consumer that validates machine identity through Machine Service/Redis cache before persisting simulator readings.
- Added Kafka telemetry consumers to Analytics and Notification. They cache the latest event and counters in Redis; Notification records a short-lived fault alert for `FAULT` events.
- Added Flyway schemas and basic validated CRUD/progress endpoints for maintenance records and production orders.
- Added multi-stage Dockerfiles, `.env.example`, a verification script, setup notes, event-flow diagrams and API examples.
- AI service source and functionality were intentionally not expanded.

## Verification performed in this environment

- All Maven `pom.xml` files parsed as valid XML.
- Docker Compose and Spring application YAML files parsed successfully with Ruby's YAML parser.
- Every service build context referenced by Compose has a Dockerfile.
- A Maven build could not be completed because the Maven Wrapper attempted to download Maven from Maven Central and the execution environment could not fetch it.
- Docker/Compose is not installed in the execution environment, so container startup and health checks could not be run here. Do not interpret static configuration checks as proof of successful runtime startup.

## Known boundaries / next engineering steps

- Notification currently records fault events in Redis and logs them; SMTP/SMS/webhook delivery requires a configured delivery provider and retry policy.
- Analytics currently caches the latest event and a total event counter; aggregation windows, historical queries, and dashboards remain future work.
- The simulator represents a device by publishing synthetic JSON telemetry. For physical hardware, implement and secure an MQTT/OPC-UA adapter that maps the device protocol to the same ingest event contract.
- Kafka uses single-broker, replication-factor-one settings for local development only. Production needs multiple brokers, ACLs/TLS, schema governance, retry/DLQ topics, idempotency, and monitoring.
- Database passwords in the checked-in application YAML are legacy development defaults. Use `.env` overrides locally and a secret manager outside development.
- The maintenance and production APIs are initial CRUD baselines; add authorization policies, pagination, audit history, domain-specific validation, and integration tests before production deployment.
- No Git commit or push was performed.
