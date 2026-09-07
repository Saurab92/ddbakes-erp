package com.bakery.inventory.dto;

import java.math.BigDecimal;

/**
 * Total consumption (quantity issued) for a single product within the
 * requested filters.
 */
public class ProductConsumptionResponse {

    private Long productId;
    private String productName;
    private String unitName;
    private BigDecimal totalQuantity;
    private Long issueCount;

    public ProductConsumptionResponse() {
    }

    public ProductConsumptionResponse(Long productId, String productName, String unitName,
                                       BigDecimal totalQuantity, Long issueCount) {
        this.productId = productId;
        this.productName = productName;
        this.unitName = unitName;
        this.totalQuantity = totalQuantity;
        this.issueCount = issueCount;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Long getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Long issueCount) {
        this.issueCount = issueCount;
    }
}
