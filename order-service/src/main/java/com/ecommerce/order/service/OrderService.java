package com.ecommerce.order.service;

import com.ecommerce.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private static final String TOPIC = "order-events";
    
    private final ConcurrentHashMap<String, Order> orderDatabase = new ConcurrentHashMap<>();
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Order createOrder(Order order) {
        order.setId(UUID.randomUUID().toString());
        order.setStatus("PENDING");
        orderDatabase.put(order.getId(), order);
        
        // Publish event to Kafka
        kafkaTemplate.send(TOPIC, "order-created", order);
        
        return order;
    }

    public Order getOrder(String id) {
        return orderDatabase.get(id);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderDatabase.values());
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderDatabase.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();
    }

    public Order updateOrderStatus(String id, String status) {
        Order order = orderDatabase.get(id);
        if (order != null) {
            order.setStatus(status);
            orderDatabase.put(id, order);
            
            // Publish event to Kafka
            kafkaTemplate.send(TOPIC, "order-status-updated", order);
            
            return order;
        }
        return null;
    }

    @KafkaListener(topics = "stock-events", groupId = "order-service-group")
    public void handleStockEvents(String message) {
        System.out.println("Received stock event: " + message);
        // Handle stock-related order updates
    }

    @KafkaListener(topics = "billing-events", groupId = "order-service-group")
    public void handleBillingEvents(String message) {
        System.out.println("Received billing event: " + message);
        // Handle billing-related order updates
    }
}
