#!/bin/bash

echo "Starting Dev Environment for AI Interview Copilot..."

# Bring up the containers in detached mode, rebuilding if necessary
docker-compose up -d --build

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
