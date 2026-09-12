package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.SupplierCreateRequest;
import com.bakery.inventory.dto.SupplierResponse;
import com.bakery.inventory.dto.SupplierUpdateRequest;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Supplier;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.SupplierMapper;
import com.bakery.inventory.repository.ProductRepository;
import com.bakery.inventory.repository.SupplierRepository;
import com.bakery.inventory.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository,
                                ProductRepository productRepository,
                                SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
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
        syncProductLinks(saved, request.getProductIds());
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
        if (request.getProductIds() != null) {
            syncProductLinks(updated, request.getProductIds());
        }
        return supplierMapper.toResponse(updated);
    }

    /**
     * Updates the owning side of the Product-Supplier relationship: adds this
     * supplier to newly linked products and removes it from products that are
     * no longer linked.
     */
    private void syncProductLinks(Supplier supplier, List<Long> productIds) {
        Set<Product> desiredProducts = new HashSet<>();
        if (productIds != null) {
            for (Long productId : productIds) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));
                desiredProducts.add(product);
            }
        }

        Set<Product> currentProducts = new HashSet<>(supplier.getProducts());

        for (Product product : desiredProducts) {
            if (!currentProducts.contains(product)) {
                product.getSuppliers().add(supplier);
            }
        }
        for (Product product : currentProducts) {
            if (!desiredProducts.contains(product)) {
                product.getSuppliers().remove(supplier);
            }
        }

        Set<Product> affected = new HashSet<>(desiredProducts);
        affected.addAll(currentProducts);
        productRepository.saveAll(affected);

        // Inverse side (mappedBy) is not auto-synced in-memory; update it so
        // the mapped response reflects the change without a re-fetch.
        Set<Product> supplierProducts = supplier.getProducts();
        supplierProducts.clear();
        supplierProducts.addAll(desiredProducts);
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
        return supplierRepository.findAllWithProducts()
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
