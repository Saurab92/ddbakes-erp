package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.RoleCreateRequest;
import com.bakery.inventory.dto.RoleResponse;
import com.bakery.inventory.dto.RoleUpdateRequest;
import com.bakery.inventory.entity.Role;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.RoleMapper;
import com.bakery.inventory.repository.RoleRepository;
import com.bakery.inventory.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleResponse createRole(RoleCreateRequest request, Long currentUserId) {
        if (roleRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Role with name '" + request.getName() + "' already exists");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);
        role.setCreatedBy(currentUserId);
        role.setUpdatedBy(currentUserId);

        Role saved = roleRepository.save(role);
        return roleMapper.toResponse(saved);
    }

    @Override
    public RoleResponse updateRole(Long roleId, RoleUpdateRequest request, Long currentUserId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(role.getName())) {
            if (roleRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Role with name '" + request.getName() + "' already exists");
            }
            role.setName(request.getName());
        }

        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }

        if (request.getActive() != null) {
            role.setActive(request.getActive());
        }

        role.setUpdatedBy(currentUserId);
        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Override
    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found with id " + roleId);
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleResponse setRoleActiveStatus(Long roleId, boolean active, Long currentUserId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));

        role.setActive(active);
        role.setUpdatedBy(currentUserId);
        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
        return roleMapper.toResponse(role);
    }
}
