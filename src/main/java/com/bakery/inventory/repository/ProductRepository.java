package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Product> findByNameIgnoreCase(String name);

    boolean existsByUnitId(Long unitId);

    @Query("select distinct p from Product p left join fetch p.suppliers")
    List<Product> findAllWithSuppliers();
}
