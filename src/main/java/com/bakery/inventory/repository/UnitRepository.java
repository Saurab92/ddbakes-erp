package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByCodeIgnoreCase(String code);

    Optional<Unit> findByCodeIgnoreCase(String code);
}
