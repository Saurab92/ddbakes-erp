package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.ProductResponse;
import com.bakery.inventory.dto.SupplierSummaryResponse;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        response.setSuppliers(toSupplierSummaries(product.getSuppliers()));
        return response;
    }

    private List<SupplierSummaryResponse> toSupplierSummaries(java.util.Set<Supplier> suppliers) {
        if (suppliers == null) {
            return List.of();
        }
        return suppliers.stream()
                .map(supplier -> new SupplierSummaryResponse(
                        supplier.getId(),
                        supplier.getName(),
                        supplier.getContactPerson(),
                        supplier.getPhone(),
                        supplier.getActive()))
                .sorted(Comparator.comparing(SupplierSummaryResponse::getId))
                .collect(Collectors.toList());
    }
}

