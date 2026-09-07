package com.bakery.inventory.repository;

import com.bakery.inventory.entity.IssueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IssueItemRepository extends JpaRepository<IssueItem, Long> {

    /**
     * Aggregates total quantity issued and issue count per department, applying
     * optional date range and department/product filters (null = no filter).
     */
    @Query("SELECT ii.issue.department.id, ii.issue.department.name, SUM(ii.quantity), COUNT(DISTINCT ii.issue.id) " +
            "FROM IssueItem ii " +
            "WHERE (:startDate IS NULL OR ii.issue.issueDate >= :startDate) " +
            "AND (:endDate IS NULL OR ii.issue.issueDate <= :endDate) " +
            "AND (:departmentId IS NULL OR ii.issue.department.id = :departmentId) " +
            "AND (:productId IS NULL OR ii.product.id = :productId) " +
            "GROUP BY ii.issue.department.id, ii.issue.department.name " +
            "ORDER BY ii.issue.department.name")
    List<Object[]> aggregateByDepartment(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("departmentId") Long departmentId,
                                          @Param("productId") Long productId);

    /**
     * Aggregates total quantity issued and issue count per product, applying
     * optional date range and department/product filters (null = no filter).
     */
    @Query("SELECT ii.product.id, ii.product.name, ii.product.unit.name, SUM(ii.quantity), COUNT(DISTINCT ii.issue.id) " +
            "FROM IssueItem ii " +
            "WHERE (:startDate IS NULL OR ii.issue.issueDate >= :startDate) " +
            "AND (:endDate IS NULL OR ii.issue.issueDate <= :endDate) " +
            "AND (:departmentId IS NULL OR ii.issue.department.id = :departmentId) " +
            "AND (:productId IS NULL OR ii.product.id = :productId) " +
            "GROUP BY ii.product.id, ii.product.name, ii.product.unit.name " +
            "ORDER BY ii.product.name")
    List<Object[]> aggregateByProduct(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("departmentId") Long departmentId,
                                       @Param("productId") Long productId);

    /**
     * Aggregates total quantity issued and issue count per day, applying optional
     * date range and department/product filters (null = no filter). Monthly totals
     * are derived from the daily buckets in the service layer.
     */
    @Query("SELECT ii.issue.issueDate, SUM(ii.quantity), COUNT(DISTINCT ii.issue.id) " +
            "FROM IssueItem ii " +
            "WHERE (:startDate IS NULL OR ii.issue.issueDate >= :startDate) " +
            "AND (:endDate IS NULL OR ii.issue.issueDate <= :endDate) " +
            "AND (:departmentId IS NULL OR ii.issue.department.id = :departmentId) " +
            "AND (:productId IS NULL OR ii.product.id = :productId) " +
            "GROUP BY ii.issue.issueDate " +
            "ORDER BY ii.issue.issueDate")
    List<Object[]> aggregateByDay(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("departmentId") Long departmentId,
                                   @Param("productId") Long productId);
}
