package com.bakery.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class IssueCreateRequest {

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Person ID is required")
    private Long personId;

    private String reason;

    private String remarks;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<IssueItemRequest> issueItems;

    public IssueCreateRequest() {
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<IssueItemRequest> getIssueItems() {
        return issueItems;
    }

    public void setIssueItems(List<IssueItemRequest> issueItems) {
        this.issueItems = issueItems;
    }
}
