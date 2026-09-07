package com.bakery.inventory.service;

import com.bakery.inventory.dto.CategoryCreateRequest;
import com.bakery.inventory.dto.CategoryResponse;
import com.bakery.inventory.dto.CategoryUpdateRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryCreateRequest request);

    CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request);

    void deleteCategory(Long categoryId);

    CategoryResponse deactivateCategory(Long categoryId);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long categoryId);
}
