package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.StockResponse;
import com.bakery.inventory.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public StockResponse toResponse(Stock stock) {
        if (stock == null) {
            return null;
        }
        return new StockResponse(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getProduct().getCategory() != null ? stock.getProduct().getCategory().getId() : null,
                stock.getProduct().getCategory() != null ? stock.getProduct().getCategory().getName() : null,
                stock.getProduct().getUnit() != null ? stock.getProduct().getUnit().getId() : null,
                stock.getProduct().getUnit() != null ? stock.getProduct().getUnit().getName() : null,
                stock.getQuantity(),
                stock.getCreatedAt(),
                stock.getUpdatedAt(),
                stock.getCreatedBy(),
                stock.getUpdatedBy()
        );
    }
}
