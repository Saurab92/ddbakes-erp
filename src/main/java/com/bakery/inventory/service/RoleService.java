package com.bakery.inventory.service;

import com.bakery.inventory.dto.RoleCreateRequest;
import com.bakery.inventory.dto.RoleResponse;
import com.bakery.inventory.dto.RoleUpdateRequest;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(RoleCreateRequest request, Long currentUserId);

    RoleResponse updateRole(Long roleId, RoleUpdateRequest request, Long currentUserId);

    void deleteRole(Long roleId);

    RoleResponse setRoleActiveStatus(Long roleId, boolean active, Long currentUserId);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(Long roleId);
}
