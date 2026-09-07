package com.bakery.inventory.controller;

import com.bakery.inventory.dto.PurchaseCreateRequest;
import com.bakery.inventory.dto.PurchaseResponse;
import com.bakery.inventory.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * Create a new purchase and add items to stock.
     *
     * @param request Purchase creation request
     * @param userId User ID from header
     * @return Created purchase response
     */
    @PostMapping
    public ResponseEntity<?> createPurchase(
            @Valid @RequestBody PurchaseCreateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        try {
            if (userId == null) {
                userId = 1L; // Default user for testing
            }

            PurchaseResponse response = purchaseService.createPurchase(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new IssueController.ErrorResponse("Validation Error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new IssueController.ErrorResponse("Error", "Failed to create purchase: " + e.getMessage())
            );
        }
    }

    /**
     * Update an existing purchase, reconciling stock for the old and new items.
     *
     * @param id Purchase ID to update
     * @param request Purchase update request
     * @param userId User ID from header
     * @return Updated purchase response
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchase(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseCreateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        try {
            if (userId == null) {
                userId = 1L; // Default user for testing
            }

            PurchaseResponse response = purchaseService.updatePurchase(id, request, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new IssueController.ErrorResponse("Not Found", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new IssueController.ErrorResponse("Error", "Failed to update purchase: " + e.getMessage())
            );
        }
    }

    /**
     * Get all purchases.
     *
     * @return List of purchases
     */
    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
        List<PurchaseResponse> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    /**
     * Get purchase by ID.
     *
     * @param id Purchase ID
     * @return Purchase details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Long id) {
        try {
            PurchaseResponse purchase = purchaseService.getPurchaseById(id);
            return ResponseEntity.ok(purchase);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new IssueController.ErrorResponse("Not Found", e.getMessage())
            );
        }
    }
}
