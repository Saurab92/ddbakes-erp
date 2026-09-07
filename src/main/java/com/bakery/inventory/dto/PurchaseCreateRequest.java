package com.bakery.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class PurchaseCreateRequest {

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    private String invoiceNumber;

    private String remarks;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<PurchaseItemRequest> purchaseItems;

    public PurchaseCreateRequest() {
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<PurchaseItemRequest> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItemRequest> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
