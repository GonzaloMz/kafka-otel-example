package com.ecommerce.stock.controller;

import com.ecommerce.common.model.Stock;
import com.ecommerce.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock createdStock = stockService.addStock(stock);
        return new ResponseEntity<>(createdStock, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable String id) {
        Stock stock = stockService.getStock(id);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable String id, @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/reserve")
    public ResponseEntity<Map<String, String>> reserveStock(@RequestBody Map<String, Object> request) {
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");
        
        boolean reserved = stockService.reserveStock(productId, quantity);
        if (reserved) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Stock reserved"));
        }
        return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Insufficient stock"));
    }
}
