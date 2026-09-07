package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.ConsumptionGroupBy;
import com.bakery.inventory.dto.ConsumptionTrendResponse;
import com.bakery.inventory.dto.DepartmentConsumptionResponse;
import com.bakery.inventory.dto.ProductConsumptionResponse;
import com.bakery.inventory.repository.IssueItemRepository;
import com.bakery.inventory.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter DAILY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTHLY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    private final IssueItemRepository issueItemRepository;

    public ReportServiceImpl(IssueItemRepository issueItemRepository) {
        this.issueItemRepository = issueItemRepository;
    }

    @Override
    public List<DepartmentConsumptionResponse> getDepartmentWiseConsumption(LocalDate startDate, LocalDate endDate,
                                                                             Long departmentId, Long productId) {
        List<Object[]> rows = issueItemRepository.aggregateByDepartment(startDate, endDate, departmentId, productId);
        return rows.stream()
                .map(row -> new DepartmentConsumptionResponse(
                        (Long) row[0],
                        (String) row[1],
                        (BigDecimal) row[2],
                        (Long) row[3]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductConsumptionResponse> getProductWiseConsumption(LocalDate startDate, LocalDate endDate,
                                                                       Long departmentId, Long productId) {
        List<Object[]> rows = issueItemRepository.aggregateByProduct(startDate, endDate, departmentId, productId);
        return rows.stream()
                .map(row -> new ProductConsumptionResponse(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        (BigDecimal) row[3],
                        (Long) row[4]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsumptionTrendResponse> getConsumptionTrend(LocalDate startDate, LocalDate endDate,
                                                               Long departmentId, Long productId,
                                                               ConsumptionGroupBy groupBy) {
        List<Object[]> rows = issueItemRepository.aggregateByDay(startDate, endDate, departmentId, productId);

        if (groupBy == ConsumptionGroupBy.MONTHLY) {
            return aggregateToMonthly(rows);
        }
        return rows.stream()
                .map(row -> new ConsumptionTrendResponse(
                        ((LocalDate) row[0]).format(DAILY_FORMAT),
                        (BigDecimal) row[1],
                        (Long) row[2]))
                .collect(Collectors.toList());
    }

    /**
     * Collapses daily aggregation rows (already sorted by date) into monthly
     * buckets, preserving chronological order.
     */
    private List<ConsumptionTrendResponse> aggregateToMonthly(List<Object[]> dailyRows) {
        Map<String, BigDecimal> quantityByMonth = new LinkedHashMap<>();
        Map<String, Long> issueCountByMonth = new LinkedHashMap<>();

        for (Object[] row : dailyRows) {
            LocalDate date = (LocalDate) row[0];
            BigDecimal quantity = (BigDecimal) row[1];
            Long issueCount = (Long) row[2];
            String monthKey = date.format(MONTHLY_FORMAT);

            quantityByMonth.merge(monthKey, quantity, BigDecimal::add);
            issueCountByMonth.merge(monthKey, issueCount, Long::sum);
        }

        return quantityByMonth.entrySet().stream()
                .map(entry -> new ConsumptionTrendResponse(
                        entry.getKey(),
                        entry.getValue(),
                        issueCountByMonth.get(entry.getKey())))
                .collect(Collectors.toList());
    }
}
