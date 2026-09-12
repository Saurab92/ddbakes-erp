package com.bakery.inventory.dto;

/**
 * Lightweight product payload used when nesting products inside a
 * supplier response, to avoid circular full-object serialization.
 */
public class ProductSummaryResponse {

    private Long id;
    private String name;
    private String unitName;
    private String categoryName;
    private Boolean active;

    public ProductSummaryResponse() {
    }

    public ProductSummaryResponse(Long id, String name, String unitName, String categoryName, Boolean active) {
        this.id = id;
        this.name = name;
        this.unitName = unitName;
        this.categoryName = categoryName;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
