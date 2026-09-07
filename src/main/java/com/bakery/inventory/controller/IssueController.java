package com.bakery.inventory.controller;

import com.bakery.inventory.dto.IssueCreateRequest;
import com.bakery.inventory.dto.IssueResponse;
import com.bakery.inventory.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * Create a new issue and deduct items from stock.
     *
     * @param request Issue creation request
     * @param userId User ID from header
     * @return Created issue response
     */
    @PostMapping
    public ResponseEntity<?> createIssue(
            @Valid @RequestBody IssueCreateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        try {
            if (userId == null) {
                userId = 1L; // Default user for testing
            }

            IssueResponse response = issueService.createIssue(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Validation Error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("Error", "Failed to create issue: " + e.getMessage())
            );
        }
    }

    /**
     * Update an existing issue, reconciling stock for the old and new items.
     *
     * @param id Issue ID to update
     * @param request Issue update request
     * @param userId User ID from header
     * @return Updated issue response
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIssue(
            @PathVariable Long id,
            @Valid @RequestBody IssueCreateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        try {
            if (userId == null) {
                userId = 1L; // Default user for testing
            }

            IssueResponse response = issueService.updateIssue(id, request, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Validation Error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("Error", "Failed to update issue: " + e.getMessage())
            );
        }
    }

    /**
     * Get all issues.
     *
     * @return List of issues
     */
    @GetMapping
    public ResponseEntity<List<IssueResponse>> getAllIssues() {
        List<IssueResponse> issues = issueService.getAllIssues();
        return ResponseEntity.ok(issues);
    }

    /**
     * Get issue by ID.
     *
     * @param id Issue ID
     * @return Issue details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getIssueById(@PathVariable Long id) {
        try {
            IssueResponse issue = issueService.getIssueById(id);
            return ResponseEntity.ok(issue);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse("Not Found", e.getMessage())
            );
        }
    }

    /**
     * Error response DTO
     */
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
}
