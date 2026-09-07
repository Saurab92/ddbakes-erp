package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Person> findByNameIgnoreCase(String name);

    List<Person> findByNameContainingIgnoreCase(String name);

    boolean existsByDepartmentId(Long departmentId);
}
