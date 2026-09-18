package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.DesignationResponse;
import com.bakery.inventory.entity.Designation;
import org.springframework.stereotype.Component;

@Component
public class DesignationMapper {

    public DesignationResponse toResponse(Designation designation) {
        DesignationResponse response = new DesignationResponse();
        response.setId(designation.getId());
        response.setName(designation.getName());
        response.setActive(designation.getActive());
        response.setCreatedAt(designation.getCreatedAt());
        response.setUpdatedAt(designation.getUpdatedAt());
        return response;
    }
}
