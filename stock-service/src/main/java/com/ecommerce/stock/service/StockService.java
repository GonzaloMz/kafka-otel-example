package com.ecommerce.stock.service;

import com.ecommerce.stock.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    private static final String TOPIC = "stock-events";
    
    private final ConcurrentHashMap<String, Stock> stockDatabase = new ConcurrentHashMap<>();
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Stock addStock(Stock stock) {
        stock.setId(UUID.randomUUID().toString());
        stockDatabase.put(stock.getId(), stock);
        
        // Publish event to Kafka
        kafkaTemplate.send(TOPIC, "stock-added", stock);
        
        return stock;
    }

    public Stock getStock(String id) {
        return stockDatabase.get(id);
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stockDatabase.values());
    }

    public Stock updateStock(String id, Stock stock) {
        if (stockDatabase.containsKey(id)) {
            stock.setId(id);
            stockDatabase.put(id, stock);
            
            // Publish event to Kafka
            kafkaTemplate.send(TOPIC, "stock-updated", stock);
            
            return stock;
        }
        return null;
    }

    public boolean reserveStock(String productId, Integer quantity) {
        Stock stock = stockDatabase.get(productId);
        if (stock != null) {
            // Use synchronized block to prevent race condition
            synchronized (stock) {
                if (stock.getQuantity() >= quantity) {
                    stock.setQuantity(stock.getQuantity() - quantity);
                    stockDatabase.put(productId, stock);
                    
                    // Publish event to Kafka
                    kafkaTemplate.send(TOPIC, "stock-reserved", Map.of("productId", productId, "quantity", quantity));
                    
                    return true;
                }
            }
        }
        return false;
    }

    @KafkaListener(topics = "order-events", groupId = "stock-service-group")
    public void handleOrderEvents(String message) {
        System.out.println("Received order event: " + message);
        // Handle order-related stock updates
    }
}
