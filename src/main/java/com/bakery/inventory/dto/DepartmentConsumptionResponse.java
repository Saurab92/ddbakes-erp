package com.bakery.inventory.dto;

import java.math.BigDecimal;

/**
 * Total consumption (quantity issued) for a single department within the
 * requested filters.
 */
public class DepartmentConsumptionResponse {

    private Long departmentId;
    private String departmentName;
    private BigDecimal totalQuantity;
    private Long issueCount;

    public DepartmentConsumptionResponse() {
    }

    public DepartmentConsumptionResponse(Long departmentId, String departmentName,
                                          BigDecimal totalQuantity, Long issueCount) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.totalQuantity = totalQuantity;
        this.issueCount = issueCount;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
