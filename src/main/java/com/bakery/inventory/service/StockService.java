package com.bakery.inventory.service;

import com.bakery.inventory.dto.StockCreateRequest;
import com.bakery.inventory.dto.StockResponse;
import com.bakery.inventory.dto.StockUpdateRequest;

public interface StockService {

    StockResponse createStock(StockCreateRequest request);

    StockResponse getStockByProductId(Long productId);

    StockResponse getStockById(Long stockId);

    StockResponse updateStock(Long stockId, StockUpdateRequest request);

    void deleteStock(Long stockId);
}
