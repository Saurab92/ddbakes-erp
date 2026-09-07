package com.bakery.inventory.service;

import com.bakery.inventory.dto.UnitCreateRequest;
import com.bakery.inventory.dto.UnitResponse;
import com.bakery.inventory.dto.UnitUpdateRequest;

import java.util.List;

public interface UnitService {

    UnitResponse createUnit(UnitCreateRequest request);

    UnitResponse updateUnit(Long unitId, UnitUpdateRequest request);

    void deleteUnit(Long unitId);

    List<UnitResponse> getAllUnits();

    UnitResponse getUnitById(Long unitId);
}
