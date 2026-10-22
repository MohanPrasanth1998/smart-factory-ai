# API collection and service verification

## Files

- `postman/Smart-Factory-API-Collection.postman_collection.json` — service API requests and smoke/workflow checks.
- `postman/Smart-Factory-Develop.postman_environment.json` — local development URLs.
- `postman/Smart-Factory-QA.postman_environment.json` — local QA URLs (same host ports by default; change values for remote QA).
- `scripts/api-smoke-check.sh` — lightweight curl checks for service reachability.

## Run with Postman

1. Start the stack with the intended profile, for example:
   ```powershell
   docker compose up --build -d
   ```
   For QA, follow `docs/PROFILES_DEVELOP_QA.md` and use its Compose command.
2. Import the collection and the Develop or QA environment into Postman.
3. Select the environment. Run requests in order, especially factory creation before machine creation, and machine creation before telemetry/simulator/maintenance/production.
4. Check response bodies and Postman test results. The collection uses returned factory/machine/record IDs where available.
5. Do not use the sample password outside local testing. Replace it with a unique development-only secret.

## Service URLs

| Service | Local base URL |
|---|---|
| Auth | `http://localhost:7080` |
| Factory | `http://localhost:7070` |
| Machine | `http://localhost:7072` |
| Telemetry | `http://localhost:7073` |
| Device Simulator | `http://localhost:7074` |
| Maintenance | `http://localhost:7075` |
| Production | `http://localhost:7076` |
| Analytics | `http://localhost:7077` |
| Notification | `http://localhost:7078` |
| API Gateway | `http://localhost:9090` |

## Important interpretation notes

- The collection is built from controller mappings found in the source. It is a test harness, not proof that the endpoints pass against a running stack.
- Health requests accept common diagnostic status codes (200, 401, 403, 404, 503) so you can inspect whether Actuator is enabled, protected, missing, or reporting a dependency issue. A 404/503 is **not** a healthy result.
- Analytics and Notification currently have no business REST controllers in the source inspected; health/log/Kafka/database verification is needed for them.
- API Gateway currently routes auth, users and factories only. Other services are tested directly unless gateway routes are added.
- The telemetry DTO requires a valid ISO-8601 timestamp, a state (`RUNNING`, `IDLE`, `STOPPED`, `FAULT`) and non-empty numeric `metrics`.
- If create endpoints return 400, check DTO fields/enums; 401/403 indicates auth/security; 404 means route/resource absent; 5xx usually indicates app/dependency/configuration errors.
- No Git commit or push is part of this workflow.

## CLI reachability checks

Linux/macOS/Git Bash:
```bash
bash scripts/api-smoke-check.sh
```

Windows PowerShell users can run individual endpoints with `Invoke-RestMethod`, e.g.:
```powershell
Invoke-RestMethod http://localhost:7074/api/v1/simulator/health
```

To verify an actual end-to-end event, run simulator `Emit one telemetry event`, then inspect telemetry history and the telemetry service logs/Kafka consumer logs. The simulator response alone does not prove the event was persisted by the consumer.
