package com.bakery.inventory.service;

import com.bakery.inventory.dto.IssueCreateRequest;
import com.bakery.inventory.dto.IssueResponse;

import java.util.List;

public interface IssueService {

    /**
     * Create a new issue and deduct from stock.
     * All items will be validated for sufficient stock before creating the issue.
     * Stock update happens atomically with issue creation.
     *
     * @param request Issue creation request with items to issue
     * @param userId ID of the user creating the issue
     * @return Created issue response
     * @throws IllegalArgumentException if any product not found or insufficient stock
     */
    IssueResponse createIssue(IssueCreateRequest request, Long userId);

    /**
     * Update an existing issue, reconciling stock for the old and new items.
     * The stock impact of the previous issue items is reversed (added back) before
     * the new items are validated for sufficient stock and deducted.
     *
     * @param id Issue ID to update
     * @param request Issue update request with the full replacement set of items
     * @param userId ID of the user updating the issue
     * @return Updated issue response
     * @throws IllegalArgumentException if issue not found, or any product not found/inactive,
     *         or insufficient stock for any new item
     */
    IssueResponse updateIssue(Long id, IssueCreateRequest request, Long userId);

    /**
     * Get all issues.
     *
     * @return List of all issues
     */
    List<IssueResponse> getAllIssues();

    /**
     * Get issue by ID.
     *
     * @param id Issue ID
     * @return Issue response
     * @throws IllegalArgumentException if issue not found
     */
    IssueResponse getIssueById(Long id);
}
