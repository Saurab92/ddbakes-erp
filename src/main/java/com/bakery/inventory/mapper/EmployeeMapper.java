package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.EmployeeResponse;
import com.bakery.inventory.entity.Department;
import com.bakery.inventory.entity.Designation;
import com.bakery.inventory.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setMobileNumber(employee.getMobileNumber());
        response.setAddress(employee.getAddress());
        response.setAadhaarNumber(employee.getAadhaarNumber());
        response.setPanNumber(employee.getPanNumber());

        Department department = employee.getDepartment();
        if (department != null) {
            response.setDepartmentId(department.getId());
            response.setDepartmentName(department.getName());
        }

        Designation designation = employee.getDesignation();
        if (designation != null) {
            response.setDesignationId(designation.getId());
            response.setDesignationName(designation.getName());
        }

        response.setJoiningDate(employee.getJoiningDate());
        response.setLastDate(employee.getLastDate());
        response.setActive(employee.getActive());
        response.setDailyWorkingHours(employee.getDailyWorkingHours());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());
        return response;
    }
}
