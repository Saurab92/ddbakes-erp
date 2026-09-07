package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.UnitResponse;
import com.bakery.inventory.entity.Unit;
import org.springframework.stereotype.Component;

@Component
public class UnitMapper {

    public UnitResponse toResponse(Unit unit) {
        UnitResponse response = new UnitResponse();
        response.setId(unit.getId());
        response.setName(unit.getName());
        response.setCode(unit.getCode());
        response.setActive(unit.getActive());
        response.setCreatedAt(unit.getCreatedAt());
        response.setUpdatedAt(unit.getUpdatedAt());
        return response;
    }
}
