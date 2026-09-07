package com.bakery.inventory.controller;

import com.bakery.inventory.dto.SupplierCreateRequest;
import com.bakery.inventory.dto.SupplierResponse;
import com.bakery.inventory.dto.SupplierUpdateRequest;
import com.bakery.inventory.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierCreateRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<SupplierResponse> getSupplierByName(@RequestParam String name) {
        return ResponseEntity.ok(supplierService.getSupplierByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Long id,
                                                             @Valid @RequestBody SupplierUpdateRequest request) {
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SupplierResponse> activateSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.activateSupplier(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SupplierResponse> deactivateSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.deactivateSupplier(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
