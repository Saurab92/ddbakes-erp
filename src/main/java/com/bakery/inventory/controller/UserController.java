package com.bakery.inventory.controller;

import com.bakery.inventory.dto.ChangePasswordRequest;
import com.bakery.inventory.dto.UserCreateRequest;
import com.bakery.inventory.dto.UserResponse;
import com.bakery.inventory.dto.UserUpdateRequest;
import com.bakery.inventory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        UserResponse response = userService.createUser(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        UserResponse response = userService.updateUser(id, request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId,
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        UserResponse response = userService.deactivateUser(id, currentUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<UserResponse> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader("X-User-Role") String currentUserRole) {
        verifyAdminRole(currentUserRole);
        UserResponse response = userService.changePassword(id, request);
        return ResponseEntity.ok(response);
    }

    private void verifyAdminRole(String role) {
        if (role == null || !role.equals("SUPER_ADMIN")) {
            throw new IllegalArgumentException("Only SUPER_ADMIN role can perform this operation");
        }
    }
}
