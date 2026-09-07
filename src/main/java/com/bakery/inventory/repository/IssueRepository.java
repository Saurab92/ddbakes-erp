package com.bakery.inventory.repository;

import com.bakery.inventory.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    boolean existsByDepartmentId(Long departmentId);

    boolean existsByPersonId(Long personId);
}
