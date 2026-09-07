package com.bakery.inventory.service;

import com.bakery.inventory.dto.ConsumptionGroupBy;
import com.bakery.inventory.dto.ConsumptionTrendResponse;
import com.bakery.inventory.dto.DepartmentConsumptionResponse;
import com.bakery.inventory.dto.ProductConsumptionResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides read-only consumption reports derived from stock issues:
 * department-wise, product-wise, and daily/monthly trend breakdowns.
 */
public interface ReportService {

    /**
     * Total quantity consumed per department for the given filters.
     *
     * @param startDate    inclusive lower bound on issue date, or null for no lower bound
     * @param endDate      inclusive upper bound on issue date, or null for no upper bound
     * @param departmentId optional department filter
     * @param productId    optional product filter
     */
    List<DepartmentConsumptionResponse> getDepartmentWiseConsumption(LocalDate startDate, LocalDate endDate,
                                                                      Long departmentId, Long productId);

    /**
     * Total quantity issued per product for the given filters.
     *
     * @param startDate    inclusive lower bound on issue date, or null for no lower bound
     * @param endDate      inclusive upper bound on issue date, or null for no upper bound
     * @param departmentId optional department filter
     * @param productId    optional product filter
     */
    List<ProductConsumptionResponse> getProductWiseConsumption(LocalDate startDate, LocalDate endDate,
                                                                Long departmentId, Long productId);

    /**
     * Consumption trend bucketed by day or month for the given filters.
     *
     * @param startDate    inclusive lower bound on issue date, or null for no lower bound
     * @param endDate      inclusive upper bound on issue date, or null for no upper bound
     * @param departmentId optional department filter
     * @param productId    optional product filter
     * @param groupBy      bucket granularity (DAILY or MONTHLY)
     */
    List<ConsumptionTrendResponse> getConsumptionTrend(LocalDate startDate, LocalDate endDate,
                                                        Long departmentId, Long productId,
                                                        ConsumptionGroupBy groupBy);
}
