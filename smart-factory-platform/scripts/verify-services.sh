#!/usr/bin/env bash
set -uo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
LOG_DIR="$ROOT/build-logs"
mkdir -p "$LOG_DIR"
status=0
while IFS= read -r pom; do
  dir="$(dirname "$pom")"
  name="$(basename "$dir")"
  echo "==> Verifying $name"
  (cd "$dir" && ./mvnw -B clean verify) >"$LOG_DIR/$name.log" 2>&1 || { echo "FAILED: $name (see build-logs/$name.log)"; status=1; }
done < <(find "$ROOT/services" -name pom.xml -not -path '*/ai/*' -print | sort)
exit "$status"
