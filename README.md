# Distributed Task Scheduler

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF.svg)

A robust, high-performance distributed background job processing system. This module is designed to handle high-throughput background tasks asynchronously, ensuring data consistency, fault tolerance, and safe concurrency across multiple worker nodes.

## Core Architecture & Mechanisms

- **Concurrency Safe (No Race Conditions):** Utilizes PostgreSQL's `FOR UPDATE SKIP LOCKED` mechanism. Multiple instances can poll the database simultaneously without acquiring overlapping locks or executing duplicate jobs.
- **High Performance I/O:** Powered by Java 21 Virtual Threads (Project Loom). Workers can process thousands of network-bound tasks (e.g., sending emails, calling 3rd-party APIs) without exhausting OS threads or HikariCP connection pools.
- **Fault Tolerance & Exponential Backoff:** Failed tasks are automatically rescheduled with an exponentially increasing delay (2, 4, 8 minutes) based on configured `max_retries`.
- **Self-Healing (Zombie Recovery):** A dedicated background sweeper automatically identifies and resets jobs that have been stuck in the `RUNNING` state due to unexpected server crashes or OOM kills.
- **Decoupled Design:** Strict separation between the `Dispatcher` polling and `Processor` execution to prevent Proxy self-invocation issues and database connection leaks.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring AOP)
- **Database:** PostgreSQL (Native JSONB support for dynamic payloads)
- **Testing:** JUnit 5, Mockito, Testcontainers
- **DevOps:** Docker, Docker Compose, GitHub Actions (CI)

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 21 & Maven

### 1. Start Infrastructure

Run the PostgreSQL database using Docker Compose:

```bash
docker-compose up -d
```

### 2. Run Tests (Continuous Integration)

The project uses Testcontainers to spin up a real PostgreSQL instance for integration testing. To verify concurrency safety (SKIP LOCKED) and logic handling:
Bash

```bash
mvn clean test
```

### 3. Build and Run

Build the application JAR and start the service:
Bash

```bash
mvn clean package -DskipTests
java -jar target/taskschedule-0.0.1-SNAPSHOT.jar
```

## Continuous Integration

This repository is configured with a GitHub Actions pipeline (.github/workflows/main.yml) that automatically triggers on every push to the main branch. It provisions an Ubuntu environment, runs the Testcontainers test suite, and verifies the Docker build process (docker build -t task-schedule:test .) to ensure zero-regression deployments.
