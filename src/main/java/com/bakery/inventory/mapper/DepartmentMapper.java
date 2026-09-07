package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.DepartmentResponse;
import com.bakery.inventory.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponse toResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setActive(department.getActive());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());
        return response;
    }
}
