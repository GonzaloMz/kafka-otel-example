# Quick Start Guide

## Prerequisites
- Docker and Docker Compose installed
- Ports 8081-8084, 9092, 9093 available

## Start the System

```bash
# Start all services with Docker Compose
docker compose up --build
```

Wait for all services to start (approximately 1-2 minutes).

## Verify Services are Running

```bash
# Check User Service
curl http://localhost:8081/actuator/health

# Check Stock Service  
curl http://localhost:8082/actuator/health

# Check Order Service
curl http://localhost:8083/actuator/health

# Check Billing Service
curl http://localhost:8084/actuator/health
```

All should return `{"status":"UP"}`

## Test the System

Run the automated test script:
```bash
./test-services.sh
```

Or test manually:

### 1. Create a User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","address":"123 Main St"}'
```

### 2. Add Stock
```bash
curl -X POST http://localhost:8082/api/stock \
  -H "Content-Type: application/json" \
  -d '{"productName":"Laptop","quantity":10,"price":999.99}'
```

### 3. List All Stock
```bash
curl http://localhost:8082/api/stock
```

### 4. Create an Order
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"<user-id-from-step-1>",
    "items":[
      {"productId":"<product-id-from-step-2>","quantity":1,"price":999.99}
    ],
    "totalAmount":999.99
  }'
```

### 5. Create an Invoice
```bash
curl -X POST http://localhost:8084/api/billing/invoices \
  -H "Content-Type: application/json" \
  -d '{
    "orderId":"<order-id-from-step-4>",
    "userId":"<user-id-from-step-1>",
    "amount":999.99
  }'
```

### 6. Process Payment
```bash
curl -X POST http://localhost:8084/api/billing/invoices/<invoice-id>/pay
```

## View Logs

```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f user-service
docker compose logs -f stock-service
docker compose logs -f order-service
docker compose logs -f billing-service
```

## Stop the System

```bash
# Stop all services
docker compose down

# Stop and remove volumes
docker compose down -v
```

## Troubleshooting

### Services not starting?
- Check if ports are available: `netstat -an | grep "808[1-4]\|9092\|9093"`
- Check Docker logs: `docker compose logs`

### Kafka connection issues?
- Wait longer for Kafka to initialize in KRaft mode (can take 30-60 seconds)
- Restart services: `docker compose restart`

### API not responding?
- Verify service health: `curl http://localhost:808X/actuator/health`
- Check service logs: `docker compose logs <service-name>`

## Service Ports

- User Service: http://localhost:8081
- Stock Service: http://localhost:8082
- Order Service: http://localhost:8083
- Billing Service: http://localhost:8084
- Kafka: localhost:9092 (client), localhost:9093 (controller)

## API Documentation

See README.md for complete API endpoint documentation.
