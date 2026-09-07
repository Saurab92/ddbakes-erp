package com.bakery.inventory.controller;

import com.bakery.inventory.dto.StockCreateRequest;
import com.bakery.inventory.dto.StockResponse;
import com.bakery.inventory.dto.StockUpdateRequest;
import com.bakery.inventory.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<StockResponse> createStock(@Valid @RequestBody StockCreateRequest request) {
        StockResponse response = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<StockResponse> getStock(@PathVariable Long stockId) {
        StockResponse response = stockService.getStockById(stockId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockResponse> getStockByProductId(@PathVariable Long productId) {
        StockResponse response = stockService.getStockByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable Long stockId, @Valid @RequestBody StockUpdateRequest request) {
        StockResponse response = stockService.updateStock(stockId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long stockId) {
        stockService.deleteStock(stockId);
        return ResponseEntity.noContent().build();
    }
}
