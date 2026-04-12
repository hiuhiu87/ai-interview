#!/bin/bash

set -e

BACKEND_SERVICES=(
	api-gateway
	interview-cms-service
	interview-session-service
	ai-worker-service
)

show_help() {
	echo "Usage: ./run-dev.sh [--be-only | --backend-only] [--help | -h]"
	echo ""
	echo "Options:"
	echo "  --be-only, --backend-only   Rebuild backend images only, keep frontend image as-is"
	echo "  --help, -h                  Show this help message"
}

echo "Starting Dev Environment for AI Interview Copilot..."

MODE="all"

while [[ $# -gt 0 ]]; do
	case "$1" in
		--be-only|--backend-only)
			MODE="backend-only"
			shift
			;;
		--help|-h)
			show_help
			exit 0
			;;
		*)
			echo "Unknown option: $1"
			echo ""
			show_help
			exit 1
			;;
	esac
done

if [[ "$MODE" == "backend-only" ]]; then
	echo "Mode: rebuild backend images only (frontend image is not rebuilt)."
	docker-compose up -d --build "${BACKEND_SERVICES[@]}"
	FRONTEND_IMAGE_ID=$(docker-compose images -q frontend 2>/dev/null || true)
	if [[ -n "$FRONTEND_IMAGE_ID" ]]; then
		docker-compose up -d --no-build frontend
	else
		echo "Skipping frontend startup in backend-only mode because frontend image is not available locally."
		echo "Run './run-dev.sh' once to build all images, or run 'docker-compose up -d --build frontend'."
	fi
else
	echo "Mode: rebuild all images (backend + frontend)."
	docker-compose up -d --build
fi

echo ""
echo "============================================="
echo "Infrastructure is spinning up!"
echo "Services will be accessible at:"
echo "- Frontend: http://localhost:5173"
echo "- API Gateway: http://localhost:8080"
echo "- RabbitMQ Management: http://localhost:15672 (guest/guest)"
echo "- Postgres on localhost:5432"
echo "============================================="
echo "Run 'docker-compose logs -f' to view all service logs."
echo "To stop the environment, run 'docker-compose down'."
