package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.ProductSummaryResponse;
import com.bakery.inventory.dto.SupplierResponse;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupplierMapper {

    public SupplierResponse toResponse(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setContactPerson(supplier.getContactPerson());
        response.setPhone(supplier.getPhone());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setActive(supplier.getActive());
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        response.setProducts(toProductSummaries(supplier.getProducts()));
        return response;
    }

    private List<ProductSummaryResponse> toProductSummaries(java.util.Set<Product> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(product -> new ProductSummaryResponse(
                        product.getId(),
                        product.getName(),
                        product.getUnit().getName(),
                        product.getCategory().getName(),
                        product.getActive()))
                .sorted(Comparator.comparing(ProductSummaryResponse::getId))
                .collect(Collectors.toList());
    }
}

