package com.bakery.inventory.controller;

import com.bakery.inventory.dto.ConsumptionGroupBy;
import com.bakery.inventory.dto.ConsumptionTrendResponse;
import com.bakery.inventory.dto.DepartmentConsumptionResponse;
import com.bakery.inventory.dto.ProductConsumptionResponse;
import com.bakery.inventory.service.ReportService;
import com.bakery.inventory.util.CsvExportUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Consumption reporting endpoints: department-wise, product-wise, and
 * daily/monthly consumption trends, derived from stock issues. Each endpoint
 * supports an optional date range plus department/product filters, and can
 * return either JSON (default) or CSV via {@code format=csv}.
 */
@RestController
@RequestMapping("/api/reports/consumption")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Department-wise consumption report: total quantity issued per department.
     */
    @GetMapping("/department-wise")
    public ResponseEntity<?> getDepartmentWiseConsumption(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false, defaultValue = "json") String format) {

        try {
            validateDateRange(startDate, endDate);
            List<DepartmentConsumptionResponse> report =
                    reportService.getDepartmentWiseConsumption(startDate, endDate, departmentId, productId);

            if (isCsv(format)) {
                String csv = CsvExportUtil.toCsv(
                        Arrays.asList("departmentId", "departmentName", "totalQuantity", "issueCount"),
                        report,
                        DepartmentConsumptionResponse::getDepartmentId,
                        DepartmentConsumptionResponse::getDepartmentName,
                        DepartmentConsumptionResponse::getTotalQuantity,
                        DepartmentConsumptionResponse::getIssueCount);
                return csvResponse(csv, "department-wise-consumption.csv");
            }
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new IssueController.ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new IssueController.ErrorResponse("Error", "Failed to generate department-wise report: " + e.getMessage()));
        }
    }

    /**
     * Product-wise consumption report: total quantity issued per product.
     */
    @GetMapping("/product-wise")
    public ResponseEntity<?> getProductWiseConsumption(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false, defaultValue = "json") String format) {

        try {
            validateDateRange(startDate, endDate);
            List<ProductConsumptionResponse> report =
                    reportService.getProductWiseConsumption(startDate, endDate, departmentId, productId);

            if (isCsv(format)) {
                String csv = CsvExportUtil.toCsv(
                        Arrays.asList("productId", "productName", "unitName", "totalQuantity", "issueCount"),
                        report,
                        ProductConsumptionResponse::getProductId,
                        ProductConsumptionResponse::getProductName,
                        ProductConsumptionResponse::getUnitName,
                        ProductConsumptionResponse::getTotalQuantity,
                        ProductConsumptionResponse::getIssueCount);
                return csvResponse(csv, "product-wise-consumption.csv");
            }
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new IssueController.ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new IssueController.ErrorResponse("Error", "Failed to generate product-wise report: " + e.getMessage()));
        }
    }

    /**
     * Daily/monthly consumption trend report.
     */
    @GetMapping("/trend")
    public ResponseEntity<?> getConsumptionTrend(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false, defaultValue = "DAILY") String groupBy,
            @RequestParam(required = false, defaultValue = "json") String format) {

        try {
            validateDateRange(startDate, endDate);
            ConsumptionGroupBy resolvedGroupBy = parseGroupBy(groupBy);
            List<ConsumptionTrendResponse> report =
                    reportService.getConsumptionTrend(startDate, endDate, departmentId, productId, resolvedGroupBy);

            if (isCsv(format)) {
                String csv = CsvExportUtil.toCsv(
                        Arrays.asList("period", "totalQuantity", "issueCount"),
                        report,
                        ConsumptionTrendResponse::getPeriod,
                        ConsumptionTrendResponse::getTotalQuantity,
                        ConsumptionTrendResponse::getIssueCount);
                return csvResponse(csv, "consumption-trend.csv");
            }
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new IssueController.ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new IssueController.ErrorResponse("Error", "Failed to generate consumption trend report: " + e.getMessage()));
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must not be after endDate");
        }
    }

    private ConsumptionGroupBy parseGroupBy(String groupBy) {
        try {
            return ConsumptionGroupBy.valueOf(groupBy.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("groupBy must be either DAILY or MONTHLY");
        }
    }

    private boolean isCsv(String format) {
        return "csv".equalsIgnoreCase(format);
    }

    private ResponseEntity<String> csvResponse(String csv, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
