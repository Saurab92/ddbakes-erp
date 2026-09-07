package com.bakery.inventory.controller;

import com.bakery.inventory.dto.UnitCreateRequest;
import com.bakery.inventory.dto.UnitResponse;
import com.bakery.inventory.dto.UnitUpdateRequest;
import com.bakery.inventory.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping
    public ResponseEntity<UnitResponse> createUnit(@Valid @RequestBody UnitCreateRequest request) {
        UnitResponse response = unitService.createUnit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UnitResponse>> getAllUnits() {
        return ResponseEntity.ok(unitService.getAllUnits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> getUnitById(@PathVariable Long id) {
        return ResponseEntity.ok(unitService.getUnitById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitResponse> updateUnit(@PathVariable Long id,
                                                    @Valid @RequestBody UnitUpdateRequest request) {
        UnitResponse response = unitService.updateUnit(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }
}
