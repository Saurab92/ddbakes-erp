package com.bakery.inventory.service;

import com.bakery.inventory.dto.ChangePasswordRequest;
import com.bakery.inventory.dto.UserCreateRequest;
import com.bakery.inventory.dto.UserResponse;
import com.bakery.inventory.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request, Long currentUserId);

    UserResponse updateUser(Long userId, UserUpdateRequest request, Long currentUserId);

    UserResponse deactivateUser(Long userId, Long currentUserId);

    UserResponse reactivateUser(Long userId, Long currentUserId);

    void deleteUser(Long userId);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    UserResponse changePassword(Long userId, ChangePasswordRequest request);
}
