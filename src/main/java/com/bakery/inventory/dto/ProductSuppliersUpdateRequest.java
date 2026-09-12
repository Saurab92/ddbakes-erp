package com.bakery.inventory.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request payload for replacing the set of suppliers linked to a product.
 */
public class ProductSuppliersUpdateRequest {

    @NotNull(message = "Supplier ids are required")
    private List<Long> supplierIds;

    public List<Long> getSupplierIds() {
        return supplierIds;
    }

    public void setSupplierIds(List<Long> supplierIds) {
        this.supplierIds = supplierIds;
    }
}
