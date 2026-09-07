package com.bakery.inventory.dto;

import jakarta.validation.constraints.Size;

/**
 * Request payload for updating a unit. All fields are optional; only
 * non-null values are applied.
 */
public class UnitUpdateRequest {

    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;

    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
