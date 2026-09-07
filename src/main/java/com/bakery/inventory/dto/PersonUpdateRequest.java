package com.bakery.inventory.dto;

import jakarta.validation.constraints.Size;

/**
 * Request payload for updating a person. All fields are optional; only
 * non-null values are applied.
 */
public class PersonUpdateRequest {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private Long departmentId;

    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
