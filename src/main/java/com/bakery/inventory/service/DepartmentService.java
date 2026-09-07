package com.bakery.inventory.service;

import com.bakery.inventory.dto.DepartmentCreateRequest;
import com.bakery.inventory.dto.DepartmentResponse;
import com.bakery.inventory.dto.DepartmentUpdateRequest;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentCreateRequest request);

    DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest request);

    void deleteDepartment(Long departmentId);

    DepartmentResponse deactivateDepartment(Long departmentId);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long departmentId);
}
