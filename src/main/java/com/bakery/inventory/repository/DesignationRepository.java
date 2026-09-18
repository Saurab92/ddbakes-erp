package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Designation> findByNameIgnoreCase(String name);
}
