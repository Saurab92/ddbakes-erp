package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.DepartmentCreateRequest;
import com.bakery.inventory.dto.DepartmentResponse;
import com.bakery.inventory.dto.DepartmentUpdateRequest;
import com.bakery.inventory.entity.Department;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceInUseException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.DepartmentMapper;
import com.bakery.inventory.repository.DepartmentRepository;
import com.bakery.inventory.repository.IssueRepository;
import com.bakery.inventory.repository.PersonRepository;
import com.bakery.inventory.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                  IssueRepository issueRepository,
                                  PersonRepository personRepository,
                                  DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Department with name '" + request.getName() + "' already exists");
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(department.getName())) {
            if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Department with name '" + request.getName() + "' already exists");
            }
            department.setName(request.getName());
        }

        if (request.getActive() != null) {
            department.setActive(request.getActive());
        }

        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponse(updated);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));

        if (issueRepository.existsByDepartmentId(department.getId())) {
            throw new ResourceInUseException(
                    "Department with id " + departmentId + " cannot be deleted because it is still referenced by issues");
        }

        if (personRepository.existsByDepartmentId(department.getId())) {
            throw new ResourceInUseException(
                    "Department with id " + departmentId + " cannot be deleted because it is still referenced by persons");
        }

        departmentRepository.delete(department);
    }

    @Override
    public DepartmentResponse deactivateDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));

        department.setActive(Boolean.FALSE);
        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));
        return departmentMapper.toResponse(department);
    }
}
