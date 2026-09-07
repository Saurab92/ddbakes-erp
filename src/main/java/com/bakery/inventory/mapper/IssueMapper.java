package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.IssueItemResponse;
import com.bakery.inventory.dto.IssueResponse;
import com.bakery.inventory.entity.Issue;
import com.bakery.inventory.entity.IssueItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IssueMapper {

    public IssueResponse toIssueResponse(Issue issue) {
        if (issue == null) {
            return null;
        }

        IssueResponse response = new IssueResponse();
        response.setId(issue.getId());
        response.setIssueDate(issue.getIssueDate());
        if (issue.getDepartment() != null) {
            response.setDepartmentId(issue.getDepartment().getId());
            response.setDepartmentName(issue.getDepartment().getName());
        }
        if (issue.getPerson() != null) {
            response.setPersonId(issue.getPerson().getId());
            response.setPersonName(issue.getPerson().getName());
        }
        response.setReason(issue.getReason());
        response.setRemarks(issue.getRemarks());
        response.setCreatedAt(issue.getCreatedAt());
        response.setUpdatedAt(issue.getUpdatedAt());
        response.setCreatedBy(issue.getCreatedBy());

        List<IssueItemResponse> issueItems = issue.getIssueItems().stream()
                .map(this::toIssueItemResponse)
                .collect(Collectors.toList());
        response.setIssueItems(issueItems);

        return response;
    }

    private IssueItemResponse toIssueItemResponse(IssueItem issueItem) {
        if (issueItem == null) {
            return null;
        }

        return new IssueItemResponse(
                issueItem.getId(),
                issueItem.getProduct().getId(),
                issueItem.getProduct().getName(),
                issueItem.getQuantity()
        );
    }
}
