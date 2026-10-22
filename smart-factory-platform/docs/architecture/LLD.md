# Smart Factory AI Platform — Low-Level Design

## 1. Document Purpose

This document describes the Low-Level Design (LLD) of the Factory Service
in the Smart Factory AI Platform.

The purpose of this document is to define:

- Class responsibilities
- Package structure
- API contracts
- DTO design
- Entity design
- Repository design
- Service-layer business logic
- Validation
- Exception handling
- Transaction boundaries
- Pagination
- Factory code generation
- Logging
- Database interaction
- Docker configuration
- Kubernetes integration
- Request and response flows
- Important design decisions

The LLD focuses on how the Factory Service is implemented internally.

---

# 2. Scope

The current scope is the Factory Service.

The Factory Service is responsible for managing factory information.

Supported operations:

- Create factory
- Get factory by business code
- Get factories with pagination
- Update factory
- Delete factory

The Factory Service does not currently manage:

- Machines
- Machine telemetry
- MQTT communication
- Kafka events
- Redis caching
- AI processing
- Predictive maintenance

These capabilities will be introduced as separate services or modules
during later phases of the platform.

---

# 3. Package Structure

Current package structure:

```text
com.smartfactory.factory
│
├── controller
│   └── FactoryController
│
├── dto
│   ├── CreateFactoryRequest
│   ├── UpdateFactoryRequest
│   ├── FactoryResponse
│   └── PageResponse
│
├── entity
│   ├── Factory
│   └── FactoryStatus
│
├── exception
│   ├── FactoryNotFoundException
│   ├── InvalidPaginationException
│   ├── ErrorResponse
│   └── GlobalExceptionHandler
│
├── repository
│   ├── FactoryRepository
│   └── FactoryRepositoryCustom
│
├── repository.impl
│   └── FactoryRepositoryCustomImpl
│
├── service
│   ├── FactoryService
│   └── FactoryServiceImpl
│
└── util
    └── FactoryCodeGenerator
4. Layered Architecture

The Factory Service follows a layered architecture.

                     Client
                       |
                       | HTTP
                       v
              +-------------------+
              | FactoryController  |
              +---------+---------+
                        |
                        | DTO
                        v
              +-------------------+
              |   FactoryService  |
              +---------+---------+
                        |
                        | Entity / Repository operations
                        v
              +-------------------+
              | FactoryRepository |
              +---------+---------+
                        |
                        | JPA / Hibernate
                        v
                 +-------------+
                 |    MySQL    |
                 +-------------+

Each layer has a clearly defined responsibility.

5. Controller Layer
Class
FactoryController
Responsibility

The controller is responsible for handling HTTP communication.

Responsibilities:

Receive HTTP requests
Map URL parameters
Map request bodies to DTOs
Trigger Bean Validation
Call the service layer
Return HTTP responses
Set appropriate HTTP status codes

The controller should not:

Contain business logic
Directly access repositories
Execute database queries
Generate factory codes
Manage transactions
6. Controller API

Base path:

/api/v1/factories

Available endpoints:

HTTP Method	Endpoint	Purpose	Success
POST	/api/v1/factories	Create factory	201
GET	/api/v1/factories/{code}	Get factory	200
GET	/api/v1/factories	Get factories	200
PUT	/api/v1/factories/{code}	Update factory	200
DELETE	/api/v1/factories/{code}	Delete factory	204
7. Create Factory API
Endpoint
POST /api/v1/factories
Request
{
  "name": "Chennai Manufacturing Plant",
  "location": "Chennai",
  "status": "ACTIVE"
}
Request DTO
CreateFactoryRequest

Fields:

name
location
status

The client does not provide the factory code.

The server generates the business code.

Validation
name
  - required
  - maximum 100 characters

location
  - optional
  - maximum 255 characters

status
  - required
Successful response

HTTP status:

201 Created

Conceptual response:

{
  "id": 5,
  "code": "FAC-266X2A",
  "name": "Chennai Manufacturing Plant",
  "location": "Chennai",
  "status": "ACTIVE",
  "createdAt": "2026-09-08T14:41:59",
  "updatedAt": "2026-09-08T14:41:59"
}
8. Get Factory API
Endpoint
GET /api/v1/factories/{code}

Example:

GET /api/v1/factories/FAC-266X2A

The factory business code is used as the public identifier.

Success

HTTP status:

200 OK
Not Found

If the factory does not exist:

FactoryNotFoundException

is thrown.

The global exception handler converts it into:

404 Not Found
9. Get Factories API
Endpoint
GET /api/v1/factories

Optional parameters:

page
size

Example:

GET /api/v1/factories?page=0&size=10

Default values:

page = 0
size = 10

The pagination is zero-based.

10. Pagination Design

The application uses custom pagination.

Request:

page
size

Response:

PageResponse<FactoryResponse>

Structure:

PageResponse
│
├── content
├── page
├── size
├── totalElements
├── totalPages
├── hasNext
└── hasPrevious

Example:

{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "hasNext": true,
  "hasPrevious": false
}
11. Pagination Algorithm

The repository calculates the database offset:

offset = page * size

Example:

page = 0
size = 10

offset = 0

Second page:

page = 1
size = 10

offset = 10

Third page:

page = 2
size = 10

offset = 20

The database query uses:

setFirstResult(offset)
setMaxResults(size)

This ensures pagination occurs at the database level.

The application does not load all records into memory and then use
subList().

12.1 Pagination Validation

Current validation rules:

page >= 0

1 <= size <= 100

Invalid examples:

page = -1
size = 10

or:

page = 0
size = 500

These result in:

InvalidPaginationException

and:

HTTP 400 Bad Request
13. Update Factory API
Endpoint
PUT /api/v1/factories/{code}

Example:

PUT /api/v1/factories/FAC-266X2A

Request:

{
  "name": "Updated Manufacturing Plant",
  "location": "Chennai",
  "status": "MAINTENANCE"
}

The factory code comes from the URL.

The factory code is not regenerated during update.

The business identifier remains stable.

14. Delete Factory API
Endpoint
DELETE /api/v1/factories/{code}

Example:

DELETE /api/v1/factories/FAC-266X2A

Successful response:

204 No Content

If the factory does not exist:

404 Not Found
15. DTO Design

DTOs are used to separate the REST API contract from the persistence
model.

Current DTOs:

CreateFactoryRequest
UpdateFactoryRequest
FactoryResponse
PageResponse<T>
16. CreateFactoryRequest

Purpose:

Represents data supplied by the client during factory creation.

Structure:

CreateFactoryRequest
│
├── name
├── location
└── status

The DTO intentionally does not contain:

id
code
createdAt
updatedAt

These fields are controlled by the server.

17. UpdateFactoryRequest

Purpose:

Represents data supplied by the client during an update.

Structure:

UpdateFactoryRequest
│
├── name
├── location
└── status

The factory code is provided through:

/api/v1/factories/{code}

rather than the request body.

18. FactoryResponse

Purpose:

Represents factory information returned to clients.

Structure:

FactoryResponse
│
├── id
├── code
├── name
├── location
├── status
├── createdAt
└── updatedAt

The response DTO prevents the JPA entity from being exposed directly.

19. PageResponse

Generic response DTO:

PageResponse<T>

Fields:

content
page
size
totalElements
totalPages
hasNext
hasPrevious

The generic design allows the pagination model to be reused:

PageResponse<FactoryResponse>

and later:

PageResponse<MachineResponse>

or:

PageResponse<UserResponse>
20. Entity Design

Entity:

Factory

Database table:

factories

Conceptual structure:

Factory
│
├── id
├── code
├── name
├── location
├── status
├── createdAt
└── updatedAt
21. Factory Entity Mapping

The JPA entity maps Java properties to the database table.

Conceptually:

Java Property	Database Column
id	id
code	code
name	name
location	location
status	status
createdAt	created_at
updatedAt	updated_at

The database uses:

factories

while JPQL uses:

Factory

because JPQL operates on entity names.

22. Factory Status

Enum:

FactoryStatus

Values:

ACTIVE
INACTIVE
MAINTENANCE

The enum is persisted using:

EnumType.STRING

Therefore the database stores:

ACTIVE
INACTIVE
MAINTENANCE

rather than:

0
1
2

This prevents enum ordinal problems if the enum order changes.

23. Database Design

Current table:

factories

Conceptual schema:

factories
------------------------------------------------
id             BIGINT          PRIMARY KEY
code           VARCHAR(50)     UNIQUE NOT NULL
name           VARCHAR(100)    NOT NULL
location       VARCHAR(...)    
status         VARCHAR(30)     NOT NULL
created_at     DATETIME        NOT NULL
updated_at     DATETIME        NOT NULL
------------------------------------------------

The exact database schema is controlled by Flyway migrations.

24. Factory Code

The factory code is the public business identifier.

Example:

FAC-266X2A

The database ID remains an internal technical identifier.

Example:

id = 5

API clients use:

FAC-266X2A

rather than:

5

This separates the public business identity from the database's
technical identifier.

25. Factory Code Generation

Class:

FactoryCodeGenerator

The generator creates a candidate code.

Format:

FAC-XXXXXX

Characters are selected from:

ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789

Example:

FAC-A82K91
FAC-7PQM2Z
FAC-266X2A
26. Code Uniqueness

Code generation alone does not guarantee uniqueness.

The service therefore checks:

existsByCode(code)

Conceptual flow:

Generate Code
      |
      v
existsByCode()
      |
   +--+--+
   |     |
  YES    NO
   |     |
   |     v
   |    Save
   |
Generate Again

The database also has a unique constraint.

This is important because application-level checking alone cannot
completely protect against concurrent requests.

Example race condition:

Request A → checks code → does not exist
Request B → checks code → does not exist
Request A → inserts
Request B → inserts

The database unique constraint provides the final protection.

27. Repository Layer

Main repository:

FactoryRepository

It extends:

JpaRepository<Factory, Long>

Responsibilities:

Persist entities
Find entities
Delete entities
Check code existence
Find entity by code

Important operations:

save()
findByCode()
existsByCode()
findById()
delete()

The repository should not contain business decisions.

28. FactoryRepositoryCustom

Custom repository interface:

FactoryRepositoryCustom

Methods:

findFactories(int page, int size)

countFactories()

The custom repository exists because the application currently uses
custom database-level pagination.

29. FactoryRepositoryCustomImpl

Implementation:

FactoryRepositoryCustomImpl

Uses:

EntityManager

The query is conceptually:

SELECT f
FROM Factory f
ORDER BY f.id

Pagination is applied through:

setFirstResult(offset)
setMaxResults(size)

The query uses:

Factory

rather than:

factories

because JPQL works with entity names.

30. Deterministic Pagination

The query uses:

ORDER BY f.id

This provides deterministic ordering.

Without ordering, the database does not guarantee that records will
always be returned in the same order.

Deterministic ordering is important for offset-based pagination.

31. Service Layer

Interface:

FactoryService

Implementation:

FactoryServiceImpl

The service layer contains business logic.

Responsibilities:

Generate factory codes
Check code uniqueness
Create factories
Find factories
Update factories
Delete factories
Map entities to DTOs
Calculate pagination metadata
Define transaction boundaries
Log important business operations
32. Create Factory Service Flow
Client
  |
  | POST /api/v1/factories
  v
FactoryController
  |
  | CreateFactoryRequest
  v
FactoryService
  |
  | Generate code
  v
FactoryCodeGenerator
  |
  | FAC-XXXXXX
  v
FactoryRepository
  |
  | existsByCode()
  v
Database
  |
  | code available
  v
FactoryService
  |
  | create entity
  v
FactoryRepository
  |
  | save()
  v
MySQL
  |
  | saved entity
  v
FactoryService
  |
  | map entity → DTO
  v
FactoryController
  |
  | 201 Created
  v
Client
33. Create Factory Sequence
Client          Controller       Service       Generator       Repository       DB
  |                 |               |              |               |             |
  | POST            |               |              |               |             |
  |---------------->|               |              |               |             |
  |                 | create()      |              |               |             |
  |                 |-------------->|              |               |             |
  |                 |               | generate()   |               |             |
  |                 |               |------------->|               |             |
  |                 |               |<-------------|               |             |
  |                 |               | existsByCode |               |             |
  |                 |               |----------------------------->|             |
  |                 |               |              |               |------------>|
  |                 |               |              |               |<------------|
  |                 |               | save()       |               |             |
  |                 |               |----------------------------->|             |
  |                 |               |              |               |------------>|
  |                 |               |              |               |<------------|
  |                 |<--------------|              |               |             |
  |<----------------|               |              |               |             |
34. Get Factory Service Flow
Client
  |
  | GET /api/v1/factories/{code}
  v
FactoryController
  |
  v
FactoryService
  |
  | findByCode(code)
  v
FactoryRepository
  |
  v
MySQL

If found:

Factory
   |
   v
FactoryResponse
   |
   v
HTTP 200

If not found:

Optional.empty()
      |
      v
FactoryNotFoundException
      |
      v
GlobalExceptionHandler
      |
      v
HTTP 404
35. Update Factory Service Flow
Client
  |
  | PUT /api/v1/factories/{code}
  v
FactoryController
  |
  | UpdateFactoryRequest
  v
FactoryService
  |
  | findByCode()
  v
FactoryRepository
  |
  v
Factory
  |
  | update fields
  v
FactoryRepository
  |
  | save()
  v
MySQL

The following fields are updated:

name
location
status
updatedAt

The following fields remain unchanged:

id
code
createdAt
36. Delete Factory Service Flow
Client
  |
  | DELETE /api/v1/factories/{code}
  v
FactoryController
  |
  v
FactoryService
  |
  | findByCode()
  v
FactoryRepository
  |
  | delete()
  v
MySQL
  |
  v
HTTP 204

If the factory does not exist:

FactoryNotFoundException
        |
        v
HTTP 404
37. Entity-to-DTO Mapping

The service maps:

Factory

to:

FactoryResponse

Conceptually:

Factory Entity
      |
      | mapping
      v
FactoryResponse DTO

The controller returns the DTO rather than the entity.

This prevents persistence concerns from leaking into the API layer.

38. DTO-to-Entity Mapping

Create request:

CreateFactoryRequest

is converted into:

Factory

The service sets server-controlled fields:

id
code
createdAt
updatedAt

The client controls:

name
location
status
39. Transaction Design

Transactions are defined at the service layer.

Write operations:

createFactory()
updateFactoryByCode()
deleteFactoryByCode()

are transactional.

Read operations can use:

@Transactional(readOnly = true)

Examples:

getFactoryByCode()
getAllFactories()

The service layer is the transaction boundary because one business
operation may contain multiple repository operations.

40. Exception Handling Architecture

The application uses:

@RestControllerAdvice

through:

GlobalExceptionHandler

Architecture:

Controller
    |
    v
Service
    |
    | exception
    v
GlobalExceptionHandler
    |
    v
ErrorResponse
    |
    v
Client

This keeps exception-to-HTTP mapping centralized.

41. FactoryNotFoundException

Purpose:

Represents a business condition where the requested factory does not
exist.

Example:

Factory with code 'FAC-ABC123' not found

The exception is mapped to:

HTTP 404 Not Found
42. InvalidPaginationException

Purpose:

Represents invalid pagination parameters.

Examples:

Page must be greater than or equal to 0

or:

Size must be between 1 and 100

Mapped to:

HTTP 400 Bad Request
43. Validation Exception

Bean Validation failures are handled using:

MethodArgumentNotValidException

Example request:

{
  "name": "",
  "location": "Chennai",
  "status": null
}

Validation produces field-level errors.

Conceptually:

{
  "errors": {
    "name": "Factory name is required",
    "status": "Factory status is required"
  }
}
44. Invalid JSON / Enum Handling

Malformed request bodies can result in:

HttpMessageNotReadableException

Example:

{
  "status": ""
}

Because status is an enum, an empty string cannot be converted into
a valid FactoryStatus.

The exception is mapped to:

HTTP 400 Bad Request

This is different from Bean Validation.

Invalid JSON / enum conversion
        ↓
HttpMessageNotReadableException

Valid JSON but invalid field value
        ↓
MethodArgumentNotValidException
45. Database Constraint Handling

Database constraint violations can result in:

DataIntegrityViolationException

For example, attempting to insert a duplicate factory code.

The application maps this condition to:

HTTP 409 Conflict

The database remains the final authority for uniqueness constraints.

46. ErrorResponse

Common error response structure:

ErrorResponse
│
├── timestamp
├── status
├── error
├── message
├── path
└── errors

Example:

{
  "timestamp": "2026-09-08T12:30:00Z",
  "status": 404,
  "error": "Factory not found",
  "message": "Factory with code 'FAC-ABC123' not found",
  "path": "/api/v1/factories/FAC-ABC123",
  "errors": null
}
47. Validation Flow
HTTP Request
      |
      v
@RequestBody
      |
      v
@Valid
      |
      v
Bean Validation
      |
   +--+--+
   |     |
Valid   Invalid
   |       |
   v       v
Service  Exception
             |
             v
      GlobalExceptionHandler
             |
             v
          HTTP 400

Validation happens before business processing.

48. Logging Design

The service uses SLF4J-based logging.

Important business events are logged.

Examples:

Factory created successfully [code=FAC-ABC123, name=Plant A]

Factory updated successfully [code=FAC-ABC123, name=Updated Plant]

Factory deleted successfully [code=FAC-ABC123]

The application should avoid:

Logging sensitive data
Logging passwords
Logging complete request bodies unnecessarily
Logging every method invocation at INFO level

Logging levels:

INFO
Business lifecycle events

DEBUG
Detailed troubleshooting information

WARN
Unexpected but recoverable conditions

ERROR
Application failures
49. Logging and Future Distributed Tracing

The current service uses application logging.

Future services will introduce distributed tracing.

Example:

Client
  |
  | Trace ID: abc123
  v
Factory Service
  |
  | Trace ID: abc123
  v
Machine Service
  |
  | Trace ID: abc123
  v
Kafka

The same trace will allow a request or event flow to be followed
across multiple services.

Future observability will use:

Micrometer
OpenTelemetry
Prometheus
Grafana
Centralized logging
50. Actuator

Spring Boot Actuator exposes operational endpoints.

Current endpoints:

/actuator/health
/actuator/health/liveness
/actuator/health/readiness

Health model:

                 Actuator
                    |
          +---------+---------+
          |                   |
          v                   v
      Liveness            Readiness
          |                   |
     "Am I alive?"       "Can I receive
                          traffic?"
51. Liveness

Endpoint:

/actuator/health/liveness

Purpose:

Determine whether the application is alive.

If a container becomes unhealthy at the liveness level, Kubernetes may
restart the container.

52. Readiness

Endpoint:

/actuator/health/readiness

Purpose:

Determine whether the application is ready to receive traffic.

If readiness fails:

Pod
 |
 v
Removed from Service traffic

The Pod does not necessarily need to be restarted.

This distinction is important in Kubernetes.

53. Docker Design

The Factory Service is packaged into a Docker image.

Image:

smart-factory/factory-service:1.0.0

Flow:

Source Code
     |
     v
Maven Build
     |
     v
Spring Boot JAR
     |
     v
Docker Image
     |
     v
Docker Container

The Dockerfile uses Java 21 runtime.

The application listens on:

7070
54. Docker Runtime Configuration

Environment-specific configuration should not be embedded into the
Docker image.

For local Docker testing, database configuration can be supplied through
environment variables.

Example:

SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

This allows the same Docker image to run in different environments.

Conceptually:

Same Docker Image
       |
       +---- Development configuration
       |
       +---- Test configuration
       |
       +---- Production configuration
55. Docker Networking

When Spring Boot runs directly on the host:

Spring Boot
    |
    v
localhost:3307
    |
    v
MySQL

When Spring Boot runs inside Docker:

Docker Container
       |
       | host.docker.internal:3307
       v
    Host Mac
       |
       v
     MySQL

Inside a container:

localhost

refers to the container itself.

It does not automatically refer to the host machine.

56. Kubernetes Deployment

The Factory Service is deployed using a Kubernetes Deployment.

Current desired replica count:

replicas: 2

Architecture:

                  Deployment
                       |
             +---------+---------+
             |                   |
             v                   v
          Pod #1              Pod #2
          :7070               :7070
             |                   |
             +---------+---------+
                       |
                Factory Service

The Deployment maintains the desired number of Pods.

If a Pod fails, Kubernetes attempts to create a replacement.

57. Kubernetes Labels

Pods use:

app: factory-service

The Deployment selector uses:

app: factory-service

The Kubernetes Service also uses:

app: factory-service

Therefore the label connects the Kubernetes resources.

Deployment
    |
    | selector
    v
app=factory-service
    ^
    |
Pod labels
    |
    ^
    |
Service selector

The selector and Pod labels must match.

58. Kubernetes Service

The Factory Service uses:

type: ClusterIP

The Service exposes:

port: 8080

and forwards to:

targetPort: 7070

Traffic flow:

Other Pod
    |
    | factory-service:8080
    v
Kubernetes Service
    |
    | targetPort: 7070
    v
Factory Service Pod
    |
    v
Spring Boot :7070
59. Port Design

The Kubernetes Service and container can use different ports.

Current configuration:

Service port = 8080

Container port = 7070

Therefore:

factory-service:8080
        |
        v
Pod:7070

containerPort documents the container's listening port.

The Kubernetes Service provides the stable network endpoint.

60. Kubernetes Liveness Probe

Configuration concept:

livenessProbe
    |
    v
/actuator/health/liveness

Kubernetes periodically calls the endpoint.

If the application repeatedly fails the liveness probe:

Liveness failure
       |
       v
Container restart
61. Kubernetes Readiness Probe

Configuration concept:

readinessProbe
    |
    v
/actuator/health/readiness

If readiness fails:

Readiness failure
       |
       v
Pod removed from Service traffic

The application can remain running while it recovers.

62. Request Lifecycle

Complete request flow:

Client
  |
  | HTTP Request
  v
FactoryController
  |
  | DTO + validation
  v
FactoryService
  |
  | Business logic
  v
FactoryRepository
  |
  | JPA
  v
Hibernate
  |
  | SQL
  v
MySQL
  |
  | Result
  v
Hibernate
  |
  v
Factory Entity
  |
  | mapping
  v
FactoryResponse
  |
  v
FactoryController
  |
  | HTTP Response
  v
Client
63. Create Request Lifecycle
POST /api/v1/factories
        |
        v
CreateFactoryRequest
        |
        v
Bean Validation
        |
        v
FactoryService
        |
        +---- Generate Code
        |
        +---- Check Uniqueness
        |
        +---- Create Entity
        |
        +---- Save
        |
        +---- Map Response
        |
        v
FactoryResponse
        |
        v
201 Created
64. Get Request Lifecycle
GET /api/v1/factories/{code}
        |
        v
FactoryController
        |
        v
FactoryService
        |
        v
findByCode()
        |
        v
FactoryRepository
        |
        v
MySQL
        |
    +---+---+
    |       |
 Found    Not Found
    |       |
    v       v
Factory   Exception
    |       |
    v       v
Response  404
    |
    v
200 OK
65. Update Request Lifecycle
PUT /api/v1/factories/{code}
        |
        v
UpdateFactoryRequest
        |
        v
Validation
        |
        v
FactoryService
        |
        v
findByCode()
        |
        v
Factory Entity
        |
        +---- update name
        +---- update location
        +---- update status
        +---- update updatedAt
        |
        v
Repository.save()
        |
        v
MySQL
        |
        v
FactoryResponse
        |
        v
200 OK
66. Delete Request Lifecycle
DELETE /api/v1/factories/{code}
        |
        v
FactoryController
        |
        v
FactoryService
        |
        v
findByCode()
        |
        v
FactoryRepository.delete()
        |
        v
MySQL
        |
        v
204 No Content
67. Concurrency Considerations

Factory code generation has a potential concurrency race.

Example:

Request A                 Request B
    |                         |
Generate FAC-ABC123      Generate FAC-ABC123
    |                         |
Check DB                  Check DB
    |                         |
Not found                 Not found
    |                         |
Insert                    Insert

Application-level checking alone is insufficient.

Therefore the database has:

UNIQUE(code)

The database constraint provides the final consistency guarantee.

If a race occurs:

DataIntegrityViolationException

is handled as a conflict.

68. Data Consistency

The Factory Service follows these principles:

Factory code must be unique.
Factory status must be valid.
Required fields must not be null.
Database constraints provide final data integrity.
Transactions protect multi-step business operations.
Flyway controls schema evolution.
69. Database Migration Strategy

Flyway manages database schema changes.

Migration files are stored under:

src/main/resources/db/migration/

Example migration concepts:

V1__create_factories_table.sql

V2__add_factory_code.sql

The database maintains Flyway metadata in:

flyway_schema_history

Migration history should not be manually modified.

70. Hibernate Schema Strategy

Hibernate is used for:

Entity Mapping
Schema Validation

Flyway is responsible for:

Schema Creation
Schema Modification
Schema Versioning

This separation prevents Hibernate from unexpectedly changing the
production database schema.

71. Performance Considerations

Factory lookup by business code uses a database uniqueness constraint
and index.

Conceptually:

GET /api/v1/factories/{code}
        |
        v
findByCode()
        |
        v
Indexed lookup

This is preferable to loading all factories and searching in Java.

Pagination also limits the amount of data returned per request.

72. Current Limitations

The current Factory Service does not yet include:

Authentication
Authorization
Redis caching
Kafka
Service discovery
API Gateway
Rate limiting
Distributed tracing
Prometheus metrics
Grafana dashboards
Horizontal Pod Autoscaler
Advanced retry policies
Circuit breakers

These will be introduced in later phases.

73. Future Extensions

The Factory Service will eventually interact with other services.

Planned architecture:

Factory Service
       |
       +------ Machine Service
       |
       +------ Kafka
       |
       +------ Analytics Service
       |
       +------ IoT Service
       |
       +------ AI Service

Future communication patterns:

REST / OpenFeign
        |
        | synchronous
        v
Microservice

Kafka
        |
        | asynchronous
        v
Microservice
74. Future Machine Service

The Machine Service will manage:

Machine
Machine status
Machine configuration
Machine-to-factory relationship

Expected relationship:

Factory
   |
   +---- Machine 1
   |
   +---- Machine 2
   |
   +---- Machine 3

The Factory Service will remain responsible for factory information,
while the Machine Service will own machine information.

75. Future Kafka Integration

The platform will eventually use Kafka for asynchronous events.

Example:

Factory Service
       |
       | FactoryCreated
       v
     Kafka
       |
       +---- Analytics Service
       |
       +---- Notification Service
       |
       +---- AI Service

Future LLD documentation will cover:

Producer
Consumer
Topic
Partition
Consumer group
Offset
Retry
Dead-letter topic
Idempotency
Event schema
76. Future Redis Integration

Redis will be introduced for use cases where caching provides a
measurable benefit.

Potential flow:

Client
  |
  v
Factory Service
  |
  v
Redis
  |
  +---- Cache Hit
  |
  +---- Cache Miss
          |
          v
        MySQL

Future LLD will document:

Cache keys
TTL
Serialization
Cache invalidation
Cache-aside strategy
77. Future IoT / MQTT Integration

Machines will eventually publish telemetry through MQTT.

Example:

Machine
   |
   | MQTT
   v
MQTT Broker
   |
   v
IoT Service
   |
   v
Kafka

Telemetry may include:

temperature
pressure
vibration
power consumption
machine status
78. Future WebSocket Integration

The platform will eventually support real-time dashboard updates.

Conceptual flow:

Machine
   |
   v
MQTT
   |
   v
IoT Service
   |
   v
Kafka
   |
   v
WebSocket Service
   |
   v
Dashboard

This avoids excessive REST polling for real-time machine data.

79. Future AI Integration

The AI Service will eventually provide factory and machine intelligence.

Potential architecture:

                 AI Service
                     |
        +------------+------------+
        |            |            |
        v            v            v
       LLM          RAG      Tool Calling
                     |            |
                     v            v
                Vector DB      Backend APIs
                                  |
                                  v
                               Agent

Potential capabilities:

Factory information queries
Machine diagnostics
Maintenance assistance
Anomaly explanation
Predictive maintenance
Natural-language operational queries
80. Design Principles

The Factory Service follows these principles:

1. Separation of concerns

Each layer has a focused responsibility.

2. DTO-based API boundaries

REST APIs do not expose JPA entities directly.

3. Business identifier separation

Public business codes are separated from technical database IDs.

4. Database integrity

Important business constraints are enforced by the database.

5. Transaction boundaries

Transactions are defined around business operations.

6. Versioned schema

Database changes are managed through Flyway.

7. Database-level pagination

Large result sets are paginated by the database.

8. Centralized exception handling

REST error responses are standardized.

9. Production-oriented observability

Health checks and logging are included from the beginning.

10. Incremental architecture

New technologies and services are introduced when the platform requires
them instead of adding unnecessary infrastructure prematurely.

81. Current Architecture Summary

The current Factory Service can be summarized as:

                         Client
                           |
                           | REST
                           v
                  +-------------------+
                  | FactoryController |
                  +---------+---------+
                            |
                            v
                  +-------------------+
                  |   FactoryService  |
                  +---------+---------+
                            |
              +-------------+-------------+
              |                           |
              v                           v
    +-------------------+       +--------------------+
    | FactoryRepository |       | FactoryCodeGenerator|
    +---------+---------+       +--------------------+
              |
              v
         +---------+
         |  MySQL  |
         +---------+

              Spring Boot Actuator
                       |
              +--------+--------+
              |                 |
              v                 v
          Liveness          Readiness
              |                 |
              +--------+--------+
                       |
                  Kubernetes
                       |
              +--------+--------+
              |                 |
              v                 v
           Pod #1            Pod #2
             :7070             :7070
              \                 /
               \               /
                +-------------+
                |   Service   |
                |  ClusterIP  |
                |    :8080    |
                +-------------+
82. Evolution of the Architecture

The Factory Service is the first building block of the platform.

The architecture will evolve incrementally:

Phase 1
Factory Service
     |
     v
MySQL + Docker + Kubernetes

Phase 2
     |
     +---- Machine Service

Phase 3
     |
     +---- Kafka

Phase 4
     |
     +---- Redis

Phase 5
     |
     +---- MQTT / IoT

Phase 6
     |
     +---- WebSocket

Phase 7
     |
     +---- Observability

Phase 8
     |
     +---- Security

Phase 9
     |
     +---- AI / RAG / Vector DB

Phase 10
     |
     +---- CI/CD

Phase 11
     |
     +---- AWS

The LLD should be updated whenever a major component or design decision
is introduced.


### Save the file

After replacing the contents:

```bash
git diff -- docs/architecture/LLD.md

Then verify:

wc -l docs/architecture/LLD.md

You now have:

docs/
├── architecture/
│   ├── HLD.md
│   └── LLD.md
└── api/
    └── factory-api.md