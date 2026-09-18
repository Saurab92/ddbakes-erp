package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeCodeIgnoreCase(String employeeCode);

    Optional<Employee> findByEmployeeCodeIgnoreCase(String employeeCode);

    boolean existsByDepartmentId(Long departmentId);

    boolean existsByDesignationId(Long designationId);
}
