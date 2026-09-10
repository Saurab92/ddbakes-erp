package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.ProductCreateRequest;
import com.bakery.inventory.dto.ProductListResponse;
import com.bakery.inventory.dto.ProductResponse;
import com.bakery.inventory.dto.ProductUpdateRequest;
import com.bakery.inventory.entity.Category;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Unit;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceInUseException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.ProductMapper;
import com.bakery.inventory.repository.CategoryRepository;
import com.bakery.inventory.repository.ProductRepository;
import com.bakery.inventory.repository.StockRepository;
import com.bakery.inventory.repository.UnitRepository;
import com.bakery.inventory.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final CategoryRepository categoryRepository;
    private final StockRepository stockRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                               UnitRepository unitRepository,
                               CategoryRepository categoryRepository,
                               StockRepository stockRepository,
                               ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
        this.categoryRepository = categoryRepository;
        this.stockRepository = stockRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Product with name '" + request.getName() + "' already exists");
        }

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Unit not found with id " + request.getUnitId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setUnit(unit);
        product.setCategory(category);
        product.setMinimumStock(request.getMinimumStock());
        product.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse getAllProducts() {
        List<ProductResponse> products = productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
        return new ProductListResponse(products);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(product.getName())) {
            if (productRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Product with name '" + request.getName() + "' already exists");
            }
            product.setName(request.getName());
        }

        if (request.getUnitId() != null) {
            Unit unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Unit not found with id " + request.getUnitId()));
            product.setUnit(unit);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id " + request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getMinimumStock() != null) {
            product.setMinimumStock(request.getMinimumStock());
        }

        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        if (stockRepository.existsByProductId(product.getId())) {
            throw new ResourceInUseException(
                    "Product with id " + productId + " cannot be deleted because it has associated stock records");
        }

        productRepository.delete(product);
    }

    @Override
    public ProductResponse deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        product.setActive(Boolean.FALSE);
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }
}
