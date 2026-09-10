package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    @Query("SELECT s FROM Stock s WHERE s.quantity < s.product.minimumStock")
    List<Stock> findLowStockItems();
}
