package com.bakery.inventory.dto;

/**
 * Lightweight supplier payload used when nesting suppliers inside a
 * product response, to avoid circular full-object serialization.
 */
public class SupplierSummaryResponse {

    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private Boolean active;

    public SupplierSummaryResponse() {
    }

    public SupplierSummaryResponse(Long id, String name, String contactPerson, String phone, Boolean active) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
