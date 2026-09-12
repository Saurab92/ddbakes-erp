package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Supplier> findByNameIgnoreCase(String name);

    @Query("select distinct s from Supplier s left join fetch s.products")
    List<Supplier> findAllWithProducts();
}
