package com.bakery.inventory.dto;

import java.math.BigDecimal;

public class IssueItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal quantity;

    public IssueItemResponse() {
    }

    public IssueItemResponse(Long id, Long productId, String productName, BigDecimal quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
