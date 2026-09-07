package com.bakery.inventory.dto;

import java.math.BigDecimal;

public class StockCreateRequest {

    private Long productId;
    private BigDecimal quantity;
    private Long createdBy;

    public StockCreateRequest() {
    }

    public StockCreateRequest(Long productId, BigDecimal quantity, Long createdBy) {
        this.productId = productId;
        this.quantity = quantity;
        this.createdBy = createdBy;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
