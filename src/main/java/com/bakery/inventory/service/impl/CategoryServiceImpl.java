package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.CategoryCreateRequest;
import com.bakery.inventory.dto.CategoryResponse;
import com.bakery.inventory.dto.CategoryUpdateRequest;
import com.bakery.inventory.entity.Category;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.CategoryMapper;
import com.bakery.inventory.repository.CategoryRepository;
import com.bakery.inventory.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Category with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(category.getName())) {
            if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Category with name '" + request.getName() + "' already exists");
            }
            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        Category updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse deactivateCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        category.setActive(Boolean.FALSE);
        Category updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
        return categoryMapper.toResponse(category);
    }
}
