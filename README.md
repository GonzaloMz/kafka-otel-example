# E-Commerce Microservices System

A distributed e-commerce system built with Spring Boot microservices, communicating via Apache Kafka.

## Architecture

This system consists of four microservices:

1. **User Service** (Port 8081) - Manages user accounts and profiles
2. **Stock Service** (Port 8082) - Handles inventory and stock management
3. **Order Service** (Port 8083) - Processes customer orders
4. **Billing Service** (Port 8084) - Manages invoices and payments

All services communicate asynchronously using Apache Kafka topics.

## Technologies

- **Spring Boot 3.2.0** - Microservices framework
- **Apache Kafka** - Message broker for inter-service communication
- **Docker & Docker Compose** - Container orchestration
- **Java 17** - Programming language

## Prerequisites

- Docker and Docker Compose installed
- Java 17+ (for local development)
- Maven 3.6+ (for local development)

## Getting Started

### Running with Docker Compose

1. Clone the repository:
```bash
git clone https://github.com/GonzaloMz/kafka-otel-example.git
cd kafka-otel-example
```

2. Build the Docker image that contains the project dependencies and is used as a shared builder image.
```bash
./create_builder_image.sh
```

3. Build and start all services:
```bash
docker compose up --build
```

This will start:
- Kafka (port 9092) running in KRaft mode
- User Service (port 8081)
- Stock Service (port 8082)
- Order Service (port 8083)
- Billing Service (port 8084)

### Running Locally (Development)

1. Start Kafka infrastructure:
```bash
docker compose up kafka
```

2. Build all services:
```bash
mvn clean install
```

3. Run each service in separate terminals:
```bash
# Terminal 1
cd user-service && mvn spring-boot:run

# Terminal 2
cd stock-service && mvn spring-boot:run

# Terminal 3
cd order-service && mvn spring-boot:run

# Terminal 4
cd billing-service && mvn spring-boot:run
```

## API Endpoints

### User Service (http://localhost:8081)
- `POST /api/users` - Create a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Stock Service (http://localhost:8082)
- `POST /api/stock` - Add new stock item
- `GET /api/stock` - Get all stock items
- `GET /api/stock/{id}` - Get stock by ID
- `PUT /api/stock/{id}` - Update stock
- `POST /api/stock/reserve` - Reserve stock for order

### Order Service (http://localhost:8083)
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get all orders
- `GET /api/orders?userId={userId}` - Get orders by user
- `GET /api/orders/{id}` - Get order by ID
- `PATCH /api/orders/{id}/status` - Update order status

### Billing Service (http://localhost:8084)
- `POST /api/billing/invoices` - Create a new invoice
- `GET /api/billing/invoices` - Get all invoices
- `GET /api/billing/invoices?userId={userId}` - Get invoices by user
- `GET /api/billing/invoices/{id}` - Get invoice by ID
- `POST /api/billing/invoices/{id}/pay` - Process payment

## Example Usage

### Create a User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","address":"123 Main St"}'
```

### Add Stock
```bash
curl -X POST http://localhost:8082/api/stock \
  -H "Content-Type: application/json" \
  -d '{"productName":"Laptop","quantity":10,"price":999.99}'
```

### Create an Order
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":"user-id","items":[{"productId":"product-id","quantity":1,"price":999.99}],"totalAmount":999.99}'
```

### Create an Invoice
```bash
curl -X POST http://localhost:8084/api/billing/invoices \
  -H "Content-Type: application/json" \
  -d '{"orderId":"order-id","userId":"user-id","amount":999.99}'
```

## Kafka Topics

The services communicate through the following Kafka topics:

- `user-events` - User creation, updates, deletions
- `stock-events` - Stock additions, updates, reservations
- `order-events` - Order creation, status updates
- `billing-events` - Invoice creation, payment processing

## Health Checks

Each service exposes actuator endpoints:
- `http://localhost:808X/actuator/health`
- `http://localhost:808X/actuator/info`

## Project Structure

```
.
├── docker-compose.yml
├── pom.xml (parent POM)
├── user-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── stock-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── order-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
└── billing-service/
    ├── src/
    ├── pom.xml
    └── Dockerfile
```

## Stopping the Services

```bash
docker compose down
```

To remove volumes as well:
```bash
docker compose down -v
```

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
