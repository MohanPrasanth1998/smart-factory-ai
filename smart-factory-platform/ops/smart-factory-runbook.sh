#!/usr/bin/env bash
# Rundeck-friendly operations entry point. Run from the deployed project directory.
set -euo pipefail
ACTION="${1:-status}"
PROFILE="${2:-qa}"
case "$PROFILE" in
  develop) OVERRIDE="docker-compose.develop.yml" ;;
  qa) OVERRIDE="docker-compose.qa.yml" ;;
  *) echo "Usage: $0 {status|logs|restart|deploy|rollback} {develop|qa} [image-tag]" >&2; exit 2 ;;
esac
COMPOSE=(docker compose -f docker-compose.yml -f "$OVERRIDE")
case "$ACTION" in
  status)
    "${COMPOSE[@]}" ps
    ;;
  logs)
    SERVICE="${3:-}"
    if [[ -n "$SERVICE" ]]; then "${COMPOSE[@]}" logs --tail=200 "$SERVICE"; else "${COMPOSE[@]}" logs --tail=200; fi
    ;;
  restart)
    SERVICE="${3:-}"
    if [[ -n "$SERVICE" ]]; then "${COMPOSE[@]}" restart "$SERVICE"; else "${COMPOSE[@]}" restart; fi
    ;;
  deploy)
    TAG="${3:?Pass an existing GHCR image tag, such as a 7-character commit SHA}"
    : "${IMAGE_PREFIX:?Set IMAGE_PREFIX to ghcr.io/<owner>/smart-factory}"
    export IMAGE_TAG="$TAG"
    "${COMPOSE[@]}" pull
    "${COMPOSE[@]}" up --no-build -d
    "${COMPOSE[@]}" ps
    ;;
  rollback)
    TAG="${3:?Pass a previous known-good GHCR image tag}"
    : "${IMAGE_PREFIX:?Set IMAGE_PREFIX to ghcr.io/<owner>/smart-factory}"
    export IMAGE_TAG="$TAG"
    "${COMPOSE[@]}" pull
    "${COMPOSE[@]}" up --no-build -d
    "${COMPOSE[@]}" ps
    ;;
  *)
    echo "Usage: $0 {status|logs|restart|deploy|rollback} {develop|qa} [service-or-image-tag]" >&2
    exit 2
    ;;
esac
