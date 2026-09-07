package com.bakery.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request payload for updating a product. All fields are optional; only
 * non-null values are applied.
 */
public class ProductUpdateRequest {

    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    private Long unitId;

    private Long categoryId;

    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum stock must not be negative")
    private BigDecimal minimumStock;

    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
