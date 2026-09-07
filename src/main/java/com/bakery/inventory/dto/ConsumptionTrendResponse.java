package com.bakery.inventory.dto;

import java.math.BigDecimal;

/**
 * Total consumption (quantity issued) for a single time bucket (day or month)
 * within the requested filters. The {@code period} is formatted as
 * {@code yyyy-MM-dd} for daily buckets or {@code yyyy-MM} for monthly buckets.
 */
public class ConsumptionTrendResponse {

    private String period;
    private BigDecimal totalQuantity;
    private Long issueCount;

    public ConsumptionTrendResponse() {
    }

    public ConsumptionTrendResponse(String period, BigDecimal totalQuantity, Long issueCount) {
        this.period = period;
        this.totalQuantity = totalQuantity;
        this.issueCount = issueCount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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
