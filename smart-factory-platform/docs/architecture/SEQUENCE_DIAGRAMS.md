# Core API sequence diagrams

## Factory CRUD

```mermaid
sequenceDiagram
  participant C as Client
  participant G as API Gateway
  participant F as Factory Service
  participant DB as MySQL
  C->>G: HTTP request /api/v1/factories
  G->>F: Route request
  F->>DB: Validate and persist via repository
  DB-->>F: Entity / result
  F-->>G: DTO + HTTP status
  G-->>C: Response
```

## Telemetry ingestion and fan-out

```mermaid
sequenceDiagram
  participant D as Device Simulator
  participant K as Kafka
  participant T as Telemetry Service
  participant P as PostgreSQL
  participant A as Analytics Service
  participant N as Notification Service
  D->>K: JSON device-ingest event (factory.telemetry.ingest.v1)
  K-->>A: factory.telemetry.v1
  K-->>N: factory.telemetry.v1
  T->>P: Persist telemetry when REST ingestion is used
  T->>K: Publish telemetry-created event
```

Note: the current simulator-to-Kafka path and REST telemetry persistence are separate entry paths. A Kafka-to-telemetry persistence consumer is a follow-up integration; this keeps device simulation decoupled from the HTTP validation flow until a durable event-consumer/retry policy is selected.
