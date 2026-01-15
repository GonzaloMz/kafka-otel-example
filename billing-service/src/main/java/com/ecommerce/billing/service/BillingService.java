package com.ecommerce.billing.service;

import com.ecommerce.common.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BillingService {

    private static final String TOPIC = "billing-events";
    
    private final ConcurrentHashMap<String, Invoice> invoiceDatabase = new ConcurrentHashMap<>();
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public Invoice createInvoice(Invoice invoice) {
        invoice.setId(UUID.randomUUID().toString());
        invoice.setStatus("PENDING");
        invoiceDatabase.put(invoice.getId(), invoice);
        
        // Publish event to Kafka
        kafkaTemplate.send(TOPIC, "invoice-created", invoice);
        
        return invoice;
    }

    public Invoice getInvoice(String id) {
        return invoiceDatabase.get(id);
    }

    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoiceDatabase.values());
    }

    public List<Invoice> getInvoicesByUserId(String userId) {
        return invoiceDatabase.values().stream()
                .filter(invoice -> invoice.getUserId().equals(userId))
                .toList();
    }

    public Invoice processPayment(String id) {
        Invoice invoice = invoiceDatabase.get(id);
        if (invoice != null) {
            invoice.setStatus("PAID");
            invoiceDatabase.put(id, invoice);
            
            // Publish event to Kafka
            kafkaTemplate.send(TOPIC, "payment-processed", invoice);
            
            return invoice;
        }
        return null;
    }

    @KafkaListener(topics = "order-events", groupId = "billing-service-group")
    public void handleOrderEvents(String message) {
        System.out.println("Received order event for billing: " + message);
        // Handle order-related billing processing
        // When an order is created, automatically create an invoice
    }
}
