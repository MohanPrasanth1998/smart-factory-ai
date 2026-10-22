#!/usr/bin/env bash
# Basic HTTP reachability smoke checks for Smart Factory services.
# Usage: bash scripts/api-smoke-check.sh [base-url-prefix]
# It checks actuator endpoints but reports 401/403/404 separately because actuator may be disabled.
set -u
check() {
  name="$1"; url="$2"
  code=$(curl -sS -o /tmp/sf-api-body.txt -w '%{http_code}' --max-time 5 "$url" 2>/tmp/sf-api-curl-error.txt || true)
  if [ -z "$code" ] || [ "$code" = "000" ]; then printf 'UNREACHABLE %-30s %s\n' "$name" "$(cat /tmp/sf-api-curl-error.txt)"; return; fi
  case "$code" in
    200) printf 'UP          %-30s HTTP %s\n' "$name" "$code" ;;
    401|403) printf 'PROTECTED   %-30s HTTP %s (auth required)\n' "$name" "$code" ;;
    404) printf 'NO-HEALTH   %-30s HTTP %s (actuator not exposed/installed)\n' "$name" "$code" ;;
    *) printf 'CHECK       %-30s HTTP %s\n' "$name" "$code" ;;
  esac
}
check 'Auth Service' 'http://localhost:7080/actuator/health'
check 'Factory Service' 'http://localhost:7070/actuator/health'
check 'Machine Service' 'http://localhost:7072/actuator/health'
check 'Telemetry Service' 'http://localhost:7073/actuator/health'
check 'Device Simulator' 'http://localhost:7074/api/v1/simulator/health'
check 'Maintenance Service' 'http://localhost:7075/actuator/health'
check 'Production Service' 'http://localhost:7076/actuator/health'
check 'Analytics Service' 'http://localhost:7077/actuator/health'
check 'Notification Service' 'http://localhost:7078/actuator/health'
check 'API Gateway' 'http://localhost:9090/actuator/health'
