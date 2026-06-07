# Docker Setup for AI Tourism Assistant Agent

This project is fully containerized using Docker and Docker Compose. This setup includes:
- **weather-server**: Java Spring Boot MCP server for weather data.
- **agent-client**: Java Spring Boot backend for travel itinerary generation.
- **agent-ui**: Vue.js frontend served by Nginx.

## Prerequisites
- Docker installed on your machine.
- Docker Compose installed on your machine.
- API keys for Gemini, Google Maps, and QWeather.

## Setup Instructions

### 1. Configure Environment Variables
Copy the `.env.example` file to a new file named `.env`:
```bash
cp .env.example .env
```
Open the `.env` file and fill in your API keys:
- `TRAVEL_AI_GEMINI_KEY`: Your Google Gemini API Key.
- `GROQ_API_KEY`: (Optional) Your Groq API Key for fallback.
- `GOOGLE_MAPS_API_KEY`: Your Google Maps JavaScript API Key.
- `WEATHER_API_KEY`: Your QWeather (Hefeng) API Key.

### 2. Build and Run
Run the following command to build the images and start the containers:
```bash
docker-compose up --build
```

### 3. Access the Application
Once the containers are running, you can access the components at:
- **Frontend (UI)**: [http://localhost](http://localhost)
- **Backend API**: [http://localhost:8080/api/v1/travel/chat](http://localhost:8080/api/v1/travel/chat)
- **Weather Server (SSE)**: [http://localhost:9000/sse](http://localhost:9000/sse)

## Architecture Notes
- The **agent-ui** container uses Nginx to serve the static Vue.js files and proxies all requests starting with `/api` to the **agent-client** container.
- The **agent-client** connects to the **weather-server** using the internal Docker network at `http://weather-server:9000/sse`.
- Multi-stage Docker builds are used to minimize image sizes and keep the production images lightweight (using Alpine-based JRE and Nginx).

## Troubleshooting
- **API Key Issues**: Ensure your `.env` file is correctly populated and that the keys have the necessary permissions.
- **Logs**: View logs for all services using `docker-compose logs -f` or for a specific service using `docker-compose logs -f [service-name]`.
- **Restarting**: To restart the setup, use `docker-compose down` and then `docker-compose up`.
