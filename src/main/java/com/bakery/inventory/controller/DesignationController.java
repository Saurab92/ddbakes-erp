package com.bakery.inventory.controller;

import com.bakery.inventory.dto.DesignationCreateRequest;
import com.bakery.inventory.dto.DesignationResponse;
import com.bakery.inventory.dto.DesignationUpdateRequest;
import com.bakery.inventory.service.DesignationService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping
    public ResponseEntity<DesignationResponse> createDesignation(@Valid @RequestBody DesignationCreateRequest request) {
        DesignationResponse response = designationService.createDesignation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DesignationResponse>> getAllDesignations() {
        return ResponseEntity.ok(designationService.getAllDesignations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignationResponse> getDesignationById(@PathVariable Long id) {
        return ResponseEntity.ok(designationService.getDesignationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesignationResponse> updateDesignation(@PathVariable Long id,
                                                                   @Valid @RequestBody DesignationUpdateRequest request) {
        DesignationResponse response = designationService.updateDesignation(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DesignationResponse> activateDesignation(@PathVariable Long id) {
        return ResponseEntity.ok(designationService.activateDesignation(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<DesignationResponse> deactivateDesignation(@PathVariable Long id) {
        return ResponseEntity.ok(designationService.deactivateDesignation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        designationService.deleteDesignation(id);
        return ResponseEntity.noContent().build();
    }
}
