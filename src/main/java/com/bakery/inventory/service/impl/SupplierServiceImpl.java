package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.SupplierCreateRequest;
import com.bakery.inventory.dto.SupplierResponse;
import com.bakery.inventory.dto.SupplierUpdateRequest;
import com.bakery.inventory.entity.Supplier;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.SupplierMapper;
import com.bakery.inventory.repository.SupplierRepository;
import com.bakery.inventory.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public SupplierResponse createSupplier(SupplierCreateRequest request) {
        if (supplierRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Supplier with name '" + request.getName() + "' already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    @Override
    public SupplierResponse updateSupplier(Long supplierId, SupplierUpdateRequest request) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplierId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(supplier.getName())) {
            if (supplierRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Supplier with name '" + request.getName() + "' already exists");
            }
            supplier.setName(request.getName());
        }

        if (request.getContactPerson() != null) {
            supplier.setContactPerson(request.getContactPerson());
        }
        if (request.getPhone() != null) {
            supplier.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            supplier.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            supplier.setAddress(request.getAddress());
        }
        if (request.getActive() != null) {
            supplier.setActive(request.getActive());
        }

        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updated);
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplierId));
        supplierRepository.delete(supplier);
    }

    @Override
    public SupplierResponse activateSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplierId));

        supplier.setActive(Boolean.TRUE);
        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updated);
    }

    @Override
    public SupplierResponse deactivateSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplierId));

        supplier.setActive(Boolean.FALSE);
        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplierId));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierByName(String name) {
        Supplier supplier = supplierRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with name '" + name + "'"));
        return supplierMapper.toResponse(supplier);
    }
}
