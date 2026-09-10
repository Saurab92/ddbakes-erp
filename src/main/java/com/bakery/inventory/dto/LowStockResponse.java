package com.bakery.inventory.dto;

import java.util.List;

public class LowStockResponse {

    private int count;
    private List<StockResponse> items;

    public LowStockResponse() {
    }

    public LowStockResponse(int count, List<StockResponse> items) {
        this.count = count;
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StockResponse> getItems() {
        return items;
    }

    public void setItems(List<StockResponse> items) {
        this.items = items;
    }
}
