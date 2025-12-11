📘 Microservices Architecture — Category & Expense System

A hands-on implementation of a production-style microservices architecture built using Spring Boot, Spring Cloud, Kafka, Redis, Eureka, and API Gateway.
This project demonstrates service discovery, distributed caching, event-driven communication, resilience patterns, and centralized API documentation.

🏗️ Architecture Overview
                         ┌─────────────────┐
                         │     Client      │
                         └────────┬────────┘
                                  │
                          API Gateway (8080)
                 ┌───────────────┼────────────────┐
                 │                                │
     ┌─────────────────────┐          ┌───────────────────────┐
     │ Category Service    │          │ Expense Service        │
     │ Port: 8085          │          │ Port: 8081             │
     │ CRUD + Cache        │          │ CRUD + Category Cleanup │
     └──────────┬──────────┘          └───────────┬────────────┘
                │                                   │
         Publishes Event                     Consumes Event
                │                                   │
         ┌──────▼────────┐                 ┌────────▼─────────┐
         │   Kafka Topic │  <-- Async -->  │ Kafka Listener    │
         └──────┬────────┘                 └────────┬─────────┘
                │                                   │
   ┌────────────▼────────────┐       ┌──────────────▼────────────┐
   │ PostgreSQL (Category)   │       │ PostgreSQL (Expense)       │
   └─────────────────────────┘       └────────────────────────────┘

                Redis (Distributed Cache)

🚀 Features
✅ 1. Microservices

Category Service

Expense Service

Isolated databases

Independent deployment

✅ 2. API Gateway

Route-based forwarding

CORS handling

Path rewriting

Planned: Rate Limiting + Circuit Breakers

✅ 3. Eureka Service Discovery

Each service auto-registers and Gateway resolves them dynamically.

✅ 4. Kafka Event-Driven Communication

Used for category deletion → expense cleanup workflow:

category_deletion_started

expense_deletion_succeeded

expense_deletion_failed

Supports Saga-style compensation.

✅ 5. Redis Caching

Distributed caching for category/expense lookups.

✅ 6. Centralized Swagger UI

Gateway exposes a single Swagger UI that aggregates all service documentation.

✅ 7. Circuit Breakers

Resilience4j integrated for internal service calls.

📂 Project Structure
/api-gateway
    └── Routing, Security, Swagger Aggregation

/category-service
    └── PostgreSQL + Redis Cache + Kafka Producer

/expense-service
    └── PostgreSQL + Kafka Consumer + Saga Compensation

/shared
    └── Shared DTOs & Kafka Events

🛠️ Technologies Used
Layer	Technology
Core Framework	Spring Boot 3.5
API Routing	Spring Cloud Gateway
Service Discovery	Eureka
Caching	Redis
Messaging	Apache Kafka
Resilience	Resilience4j
Databases	PostgreSQL
Documentation	SpringDoc OpenAPI
Build Tool	Maven
📡 Kafka Topics
Topic	Purpose
category_deletion_started	Category Service → Expense Service
expense_deletion_succeeded	Expense deletion completed
expense_deletion_failed	Expense deletion rollback
🔄 Saga Workflow (Delete Category)

1️⃣ Category Service receives DELETE /category/{id}
2️⃣ Publishes category_deletion_started event
3️⃣ Expense Service consumes event
4️⃣ Tries deleting all related expenses

If success → produces expense_deletion_succeeded

If failure → produces expense_deletion_failed

5️⃣ Category Service finalizes or restores data accordingly

This ensures consistency across services without distributed transactions.

📜 How to Run the Project Locally
1️⃣ Start Required Tools
Kafka (Docker recommended)

docker-compose.yml example:

version: '3.8'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports: 
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: localhost
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181


Start:

docker compose up -d

Redis
docker run -p 6379:6379 redis

PostgreSQL

Ensure Category & Expense DBs exist:

category
expense

2️⃣ Start Services
Eureka
cd eureka-server
mvn spring-boot:run

Category Service
cd category-service
mvn spring-boot:run

Expense Service
cd expense-service
mvn spring-boot:run

API Gateway
cd api-gateway
mvn spring-boot:run

🔎 Swagger URLs
Service	Endpoint
Gateway Swagger (Unified UI)	http://localhost:8080/swagger-ui.html
Category Docs	/api/category/v3/api-docs
Expense Docs	/api/expenses/v3/api-docs
🧪 Sample Saga Flow Test
1️⃣ Delete a category
DELETE http://localhost:8080/api/category/5

2️⃣ Category Service publishes event
category_deletion_started { id: 5 }

3️⃣ Expense Service receives → deletes expenses
4️⃣ On failure → rollback event sent
5️⃣ Category Service restores category if needed
💡 Future Enhancements

Add global rate limiting at Gateway

Introduce security (JWT + OAuth)

Implement Outbox Pattern for reliable events

Store Kafka offsets externally

Deploy on Kubernetes

Add Prometheus/Grafana monitoring

Use Zipkin/Jaeger for distributed tracing

🙌 Contributing

Open to discussions, suggestions, or improvements!
Feel free to create issues or PRs.
