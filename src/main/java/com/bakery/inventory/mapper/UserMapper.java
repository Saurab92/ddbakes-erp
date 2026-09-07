package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.UserCreateRequest;
import com.bakery.inventory.dto.UserResponse;
import com.bakery.inventory.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getCreatedBy(),
                user.getUpdatedBy()
        );
    }

    public User toUserEntity(UserCreateRequest request) {
        if (request == null) {
            return null;
        }

        return new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getRole()
        );
    }
}
