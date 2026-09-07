package com.bakery.inventory.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseResponse {

    private Long id;
    private LocalDate purchaseDate;
    private Long supplierId;
    private String supplierName;
    private String invoiceNumber;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private List<PurchaseItemResponse> purchaseItems;

    public PurchaseResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<PurchaseItemResponse> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItemResponse> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
