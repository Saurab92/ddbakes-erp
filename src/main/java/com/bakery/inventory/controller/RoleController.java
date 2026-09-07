package com.bakery.inventory.controller;

import com.bakery.inventory.dto.RoleCreateRequest;
import com.bakery.inventory.dto.RoleResponse;
import com.bakery.inventory.dto.RoleUpdateRequest;
import com.bakery.inventory.service.RoleService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody RoleCreateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        RoleResponse response = roleService.createRole(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        RoleResponse response = roleService.updateRole(id, request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RoleResponse> activateRole(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        return ResponseEntity.ok(roleService.setRoleActiveStatus(id, true, currentUserId));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<RoleResponse> deactivateRole(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        return ResponseEntity.ok(roleService.setRoleActiveStatus(id, false, currentUserId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
