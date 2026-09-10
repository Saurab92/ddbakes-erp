package com.bakery.inventory.dto;

import java.util.List;

/**
 * Response payload wrapping the list of products along with the total count.
 */
public class ProductListResponse {

    private int count;
    private List<ProductResponse> products;

    public ProductListResponse() {
    }

    public ProductListResponse(List<ProductResponse> products) {
        this.products = products;
        this.count = products != null ? products.size() : 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
        this.count = products != null ? products.size() : 0;
    }
}
