package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.DesignationCreateRequest;
import com.bakery.inventory.dto.DesignationResponse;
import com.bakery.inventory.dto.DesignationUpdateRequest;
import com.bakery.inventory.entity.Designation;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceInUseException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.DesignationMapper;
import com.bakery.inventory.repository.DesignationRepository;
import com.bakery.inventory.repository.EmployeeRepository;
import com.bakery.inventory.service.DesignationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final EmployeeRepository employeeRepository;
    private final DesignationMapper designationMapper;

    public DesignationServiceImpl(DesignationRepository designationRepository,
                                   EmployeeRepository employeeRepository,
                                   DesignationMapper designationMapper) {
        this.designationRepository = designationRepository;
        this.employeeRepository = employeeRepository;
        this.designationMapper = designationMapper;
    }

    @Override
    public DesignationResponse createDesignation(DesignationCreateRequest request) {
        if (designationRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Designation with name '" + request.getName() + "' already exists");
        }

        Designation designation = new Designation();
        designation.setName(request.getName());
        designation.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Designation saved = designationRepository.save(designation);
        return designationMapper.toResponse(saved);
    }

    @Override
    public DesignationResponse updateDesignation(Long designationId, DesignationUpdateRequest request) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id " + designationId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(designation.getName())) {
            if (designationRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Designation with name '" + request.getName() + "' already exists");
            }
            designation.setName(request.getName());
        }

        if (request.getActive() != null) {
            designation.setActive(request.getActive());
        }

        Designation updated = designationRepository.save(designation);
        return designationMapper.toResponse(updated);
    }

    @Override
    public void deleteDesignation(Long designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id " + designationId));

        if (employeeRepository.existsByDesignationId(designation.getId())) {
            throw new ResourceInUseException(
                    "Designation with id " + designationId + " cannot be deleted because it is still referenced by employees");
        }

        designationRepository.delete(designation);
    }

    @Override
    public DesignationResponse activateDesignation(Long designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id " + designationId));

        designation.setActive(Boolean.TRUE);
        Designation updated = designationRepository.save(designation);
        return designationMapper.toResponse(updated);
    }

    @Override
    public DesignationResponse deactivateDesignation(Long designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id " + designationId));

        designation.setActive(Boolean.FALSE);
        Designation updated = designationRepository.save(designation);
        return designationMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationResponse> getAllDesignations() {
        return designationRepository.findAll()
                .stream()
                .map(designationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationResponse getDesignationById(Long designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with id " + designationId));
        return designationMapper.toResponse(designation);
    }
}
