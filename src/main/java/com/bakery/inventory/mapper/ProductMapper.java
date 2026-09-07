package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.ProductResponse;
import com.bakery.inventory.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setUnitId(product.getUnit().getId());
        response.setUnitName(product.getUnit().getName());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        response.setMinimumStock(product.getMinimumStock());
        response.setActive(product.getActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
