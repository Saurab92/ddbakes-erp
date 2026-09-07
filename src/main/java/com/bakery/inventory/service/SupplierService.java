package com.bakery.inventory.service;

import com.bakery.inventory.dto.SupplierCreateRequest;
import com.bakery.inventory.dto.SupplierResponse;
import com.bakery.inventory.dto.SupplierUpdateRequest;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierCreateRequest request);

    SupplierResponse updateSupplier(Long supplierId, SupplierUpdateRequest request);

    void deleteSupplier(Long supplierId);

    SupplierResponse activateSupplier(Long supplierId);

    SupplierResponse deactivateSupplier(Long supplierId);

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse getSupplierById(Long supplierId);

    SupplierResponse getSupplierByName(String name);
}
