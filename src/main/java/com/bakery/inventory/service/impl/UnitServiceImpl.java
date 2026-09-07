package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.UnitCreateRequest;
import com.bakery.inventory.dto.UnitResponse;
import com.bakery.inventory.dto.UnitUpdateRequest;
import com.bakery.inventory.entity.Unit;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceInUseException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.UnitMapper;
import com.bakery.inventory.repository.ProductRepository;
import com.bakery.inventory.repository.UnitRepository;
import com.bakery.inventory.service.UnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final UnitMapper unitMapper;

    public UnitServiceImpl(UnitRepository unitRepository,
                            ProductRepository productRepository,
                            UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.productRepository = productRepository;
        this.unitMapper = unitMapper;
    }

    @Override
    public UnitResponse createUnit(UnitCreateRequest request) {
        if (unitRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Unit with name '" + request.getName() + "' already exists");
        }
        if (unitRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new DuplicateResourceException(
                    "Unit with code '" + request.getCode() + "' already exists");
        }

        Unit unit = new Unit();
        unit.setName(request.getName());
        unit.setCode(request.getCode());
        unit.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Unit saved = unitRepository.save(unit);
        return unitMapper.toResponse(saved);
    }

    @Override
    public UnitResponse updateUnit(Long unitId, UnitUpdateRequest request) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id " + unitId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(unit.getName())) {
            if (unitRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Unit with name '" + request.getName() + "' already exists");
            }
            unit.setName(request.getName());
        }

        if (request.getCode() != null && !request.getCode().equalsIgnoreCase(unit.getCode())) {
            if (unitRepository.existsByCodeIgnoreCase(request.getCode())) {
                throw new DuplicateResourceException(
                        "Unit with code '" + request.getCode() + "' already exists");
            }
            unit.setCode(request.getCode());
        }

        if (request.getActive() != null) {
            unit.setActive(request.getActive());
        }

        Unit updated = unitRepository.save(unit);
        return unitMapper.toResponse(updated);
    }

    @Override
    public void deleteUnit(Long unitId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id " + unitId));

        if (productRepository.existsByUnitId(unit.getId())) {
            throw new ResourceInUseException(
                    "Unit with id " + unitId + " cannot be deleted because it is still referenced by products");
        }

        unitRepository.delete(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> getAllUnits() {
        return unitRepository.findAll()
                .stream()
                .map(unitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UnitResponse getUnitById(Long unitId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id " + unitId));
        return unitMapper.toResponse(unit);
    }
}
