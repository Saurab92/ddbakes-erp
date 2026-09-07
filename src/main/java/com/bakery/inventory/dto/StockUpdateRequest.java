package com.bakery.inventory.dto;

import java.math.BigDecimal;

public class StockUpdateRequest {

    private BigDecimal quantity;
    private Long updatedBy;

    public StockUpdateRequest() {
    }

    public StockUpdateRequest(BigDecimal quantity, Long updatedBy) {
        this.quantity = quantity;
        this.updatedBy = updatedBy;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}
