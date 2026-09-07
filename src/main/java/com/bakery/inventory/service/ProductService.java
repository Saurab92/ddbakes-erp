package com.bakery.inventory.service;

import com.bakery.inventory.dto.ProductCreateRequest;
import com.bakery.inventory.dto.ProductResponse;
import com.bakery.inventory.dto.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long productId);

    ProductResponse updateProduct(Long productId, ProductUpdateRequest request);

    void deleteProduct(Long productId);

    ProductResponse deactivateProduct(Long productId);
}
