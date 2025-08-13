![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Prometheus](https://img.shields.io/badge/Monitoring-Prometheus-orange)
![Grafana](https://img.shields.io/badge/Dashboard-Grafana-yellow)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
# Distributed Device Fleet Monitor (Mini Netflix Open Connect)

A tiny Spring Boot service that **simulates a fleet of CDN edge appliances**. It exposes:
- REST APIs to list/update devices
- **Prometheus** metrics via Spring Boot Actuator for fleet health
- A scheduler that randomly changes device status to mimic real-world drift

This is a fast, public work sample designed to demonstrate **cloud-native service design, observability, and distributed fleet orchestration** — aligned with Netflix Open Connect’s themes.

## Clone + Run
```bash
git clone https://github.com/<your-username>/netflix-openconnect-mini.git
cd netflix-openconnect-mini
mvn clean spring-boot:run
```

## Quick Start
```bash
# Build & run
mvn clean spring-boot:run
# API
curl http://localhost:8080/api/devices | jq
curl http://localhost:8080/api/devices/edge-1 | jq

# Metrics
curl http://localhost:8080/actuator/prometheus
```

## Interesting Metrics (PromQL-ready)
- `cdn_devices_total`
- `cdn_devices_online`
- `cdn_devices_offline`
- `cdn_devices_avg_cpu`
- `cdn_devices_avg_throughput_mbps`

> These map to `cdn.devices.*` gauges (Micrometer will convert dots to underscores for Prometheus).

## Prometheus config
Create `prometheus.yml`:
```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: "cdn-monitor"
    static_configs:
      - targets: ["host.docker.internal:8080"]
        labels:
          instance: "local"
    metrics_path: /actuator/prometheus
```

## Optional: Docker
```Dockerfile
FROM eclipse-temurin:17-jre
ARG JAR=target/cdn-monitor-0.0.1-SNAPSHOT.jar
COPY ${JAR} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Architecture (Mermaid)
```mermaid
flowchart LR
  subgraph Client
    CLI[CLI / Curl] -->|REST| API[/Spring Boot API/]
    Grafana[Grafana UI] -->|PromQL Queries| Prometheus
  end

  subgraph Service
    API[/Spring Boot API/] --> Repo[(In-Memory Repo)]
    Ticker((Scheduler)) --> API
    API -->|Micrometer Metrics| Prometheus
  end

  subgraph Monitoring
    Prometheus[Prometheus Server] --> Grafana
  end
```
What’s happening

Ticker simulates fleet drift every 30s (device up/down, CPU, throughput).

The API exposes device data and metrics.

Prometheus scrapes /actuator/prometheus.

Grafana visualizes PromQL queries for fleet health.


## API Endpoints
- `GET /api/devices` — list all devices
- `GET /api/devices/{id}` — get a single device
- `POST /api/devices` — upsert a device
- `GET /actuator/prometheus` — metrics for Prometheus

## Example Upsert
```bash
curl -X POST http://localhost:8080/api/devices \
  -H "Content-Type: application/json" \
  -d '{
    "id":"edge-21",
    "region":"NA",
    "isp":"ISP-X",
    "online":true,
    "cpu":32.5,
    "throughputMbps":640.0
  }'
  ```

Why this project exists
This repo was created to showcase experience building cloud-native microservices, observability pipelines, and distributed fleet orchestration — the same kinds of problems tackled by Netflix’s Open Connect platform.

## Notes
- In-memory store (simple & demo-friendly). Swap with a DB for persistence.
- Scheduler randomizes CPU, throughput, and up/down state every 30s.
- Add Grafana dashboard panels for the listed metrics.
```





