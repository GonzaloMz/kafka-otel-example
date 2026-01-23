package com.ecommerce.billing.controller;

import com.ecommerce.common.model.Invoice;
import com.ecommerce.billing.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/invoices")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice createdInvoice = billingService.createInvoice(invoice);
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable String id) {
        Invoice invoice = billingService.getInvoice(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoices(@RequestParam(required = false) String userId) {
        List<Invoice> invoices;
        if (userId != null) {
            invoices = billingService.getInvoicesByUserId(userId);
        } else {
            invoices = billingService.getAllInvoices();
        }
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/invoices/{id}/pay")
    public ResponseEntity<Invoice> processPayment(@PathVariable String id) {
        Invoice invoice = billingService.processPayment(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        }
        return ResponseEntity.notFound().build();
    }
}
