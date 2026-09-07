package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.ChangePasswordRequest;
import com.bakery.inventory.dto.UserCreateRequest;
import com.bakery.inventory.dto.UserResponse;
import com.bakery.inventory.dto.UserUpdateRequest;
import com.bakery.inventory.entity.User;
import com.bakery.inventory.mapper.UserMapper;
import com.bakery.inventory.repository.UserRepository;
import com.bakery.inventory.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse createUser(UserCreateRequest request, Long currentUserId) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User user = userMapper.toUserEntity(request);
        user.setCreatedBy(currentUserId);
        user.setUpdatedBy(currentUserId);

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest request, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        user.setUpdatedBy(currentUserId);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse deactivateUser(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setActive(Boolean.FALSE);
        user.setUpdatedBy(currentUserId);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(request.getNewPassword());
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }
}
