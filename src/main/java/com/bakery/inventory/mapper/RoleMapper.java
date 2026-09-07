package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.RoleResponse;
import com.bakery.inventory.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponse toResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setActive(role.getActive());
        response.setCreatedAt(role.getCreatedAt());
        response.setCreatedBy(role.getCreatedBy());
        response.setUpdatedAt(role.getUpdatedAt());
        response.setUpdatedBy(role.getUpdatedBy());
        return response;
    }
}
