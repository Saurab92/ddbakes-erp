package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.StockCreateRequest;
import com.bakery.inventory.dto.StockResponse;
import com.bakery.inventory.dto.StockUpdateRequest;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Stock;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.StockMapper;
import com.bakery.inventory.repository.ProductRepository;
import com.bakery.inventory.repository.StockRepository;
import com.bakery.inventory.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository, ProductRepository productRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.stockMapper = stockMapper;
    }

    @Override
    public StockResponse createStock(StockCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Stock stock = new Stock(product, request.getQuantity());
        stock.setCreatedBy(request.getCreatedBy());
        stock.setUpdatedBy(request.getCreatedBy());
        Stock savedStock = stockRepository.save(stock);

        return stockMapper.toResponse(savedStock);
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStockByProductId(Long productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for product id: " + productId));

        return stockMapper.toResponse(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStockById(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + stockId));

        return stockMapper.toResponse(stock);
    }

    @Override
    public StockResponse updateStock(Long stockId, StockUpdateRequest request) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + stockId));

        stock.setQuantity(request.getQuantity());
        stock.setUpdatedBy(request.getUpdatedBy());
        Stock updatedStock = stockRepository.save(stock);

        return stockMapper.toResponse(updatedStock);
    }

    @Override
    public void deleteStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id: " + stockId));

        stockRepository.delete(stock);
    }
}
