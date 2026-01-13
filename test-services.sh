#!/bin/bash

# Test script for E-Commerce Microservices
echo "Testing E-Commerce Microservices..."
echo ""

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 5

# Test User Service
echo "Testing User Service..."
USER_RESPONSE=$(curl -s -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","address":"123 Main St"}')
echo "User created: $USER_RESPONSE"
echo ""

# Test Stock Service
echo "Testing Stock Service..."
STOCK_RESPONSE=$(curl -s -X POST http://localhost:8082/api/stock \
  -H "Content-Type: application/json" \
  -d '{"productName":"Laptop","quantity":10,"price":999.99}')
echo "Stock added: $STOCK_RESPONSE"
echo ""

# Extract product ID for order
PRODUCT_ID=$(echo $STOCK_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "Product ID: $PRODUCT_ID"
echo ""

# Extract user ID for order
USER_ID=$(echo $USER_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "User ID: $USER_ID"
echo ""

# Test Order Service
if [ -n "$USER_ID" ] && [ -n "$PRODUCT_ID" ]; then
    echo "Testing Order Service..."
    ORDER_RESPONSE=$(curl -s -X POST http://localhost:8083/api/orders \
      -H "Content-Type: application/json" \
      -d "{\"userId\":\"$USER_ID\",\"items\":[{\"productId\":\"$PRODUCT_ID\",\"quantity\":1,\"price\":999.99}],\"totalAmount\":999.99}")
    echo "Order created: $ORDER_RESPONSE"
    echo ""
    
    # Extract order ID
    ORDER_ID=$(echo $ORDER_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
    echo "Order ID: $ORDER_ID"
    echo ""
fi

# Test Billing Service
if [ -n "$USER_ID" ] && [ -n "$ORDER_ID" ]; then
    echo "Testing Billing Service..."
    INVOICE_RESPONSE=$(curl -s -X POST http://localhost:8084/api/billing/invoices \
      -H "Content-Type: application/json" \
      -d "{\"orderId\":\"$ORDER_ID\",\"userId\":\"$USER_ID\",\"amount\":999.99}")
    echo "Invoice created: $INVOICE_RESPONSE"
    echo ""
fi

# Test Health Endpoints
echo "Checking health endpoints..."
echo "User Service Health: $(curl -s http://localhost:8081/actuator/health)"
echo "Stock Service Health: $(curl -s http://localhost:8082/actuator/health)"
echo "Order Service Health: $(curl -s http://localhost:8083/actuator/health)"
echo "Billing Service Health: $(curl -s http://localhost:8084/actuator/health)"
echo ""

echo "All tests completed!"
