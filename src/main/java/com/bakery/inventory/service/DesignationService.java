package com.bakery.inventory.service;

import com.bakery.inventory.dto.DesignationCreateRequest;
import com.bakery.inventory.dto.DesignationResponse;
import com.bakery.inventory.dto.DesignationUpdateRequest;

import java.util.List;

public interface DesignationService {

    DesignationResponse createDesignation(DesignationCreateRequest request);

    DesignationResponse updateDesignation(Long designationId, DesignationUpdateRequest request);

    void deleteDesignation(Long designationId);

    DesignationResponse activateDesignation(Long designationId);

    DesignationResponse deactivateDesignation(Long designationId);

    List<DesignationResponse> getAllDesignations();

    DesignationResponse getDesignationById(Long designationId);
}
