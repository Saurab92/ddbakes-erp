package com.bakery.inventory.service;

import com.bakery.inventory.dto.PurchaseCreateRequest;
import com.bakery.inventory.dto.PurchaseResponse;

import java.util.List;

public interface PurchaseService {

    /**
     * Create a new purchase and add to stock.
     * For each purchased product, existing stock is increased by the purchased
     * quantity; if no stock row exists yet for the product, one is created.
     * Stock update happens atomically with purchase creation.
     *
     * @param request Purchase creation request with items purchased
     * @param userId ID of the user creating the purchase
     * @return Created purchase response
     * @throws IllegalArgumentException if any product is not found
     */
    PurchaseResponse createPurchase(PurchaseCreateRequest request, Long userId);

    /**
     * Update an existing purchase and reconcile stock accordingly.
     * The stock impact of the previous purchase items is reversed, then the
     * stock impact of the new purchase items is applied, all atomically.
     *
     * @param id Purchase ID to update
     * @param request Purchase update request with new items/details
     * @param userId ID of the user updating the purchase
     * @return Updated purchase response
     * @throws IllegalArgumentException if purchase or any product is not found
     */
    PurchaseResponse updatePurchase(Long id, PurchaseCreateRequest request, Long userId);

    /**
     * Get all purchases.
     *
     * @return List of all purchases
     */
    List<PurchaseResponse> getAllPurchases();

    /**
     * Get purchase by ID.
     *
     * @param id Purchase ID
     * @return Purchase response
     * @throws IllegalArgumentException if purchase not found
     */
    PurchaseResponse getPurchaseById(Long id);
}
