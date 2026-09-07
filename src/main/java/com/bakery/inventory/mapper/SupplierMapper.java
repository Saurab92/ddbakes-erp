package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.SupplierResponse;
import com.bakery.inventory.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierResponse toResponse(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setContactPerson(supplier.getContactPerson());
        response.setPhone(supplier.getPhone());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setActive(supplier.getActive());
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        return response;
    }
}
