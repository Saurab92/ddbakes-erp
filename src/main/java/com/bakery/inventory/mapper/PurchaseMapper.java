package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.PurchaseItemResponse;
import com.bakery.inventory.dto.PurchaseResponse;
import com.bakery.inventory.entity.Purchase;
import com.bakery.inventory.entity.PurchaseItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseMapper {

    public PurchaseResponse toPurchaseResponse(Purchase purchase) {
        if (purchase == null) {
            return null;
        }

        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setPurchaseDate(purchase.getPurchaseDate());
        if (purchase.getSupplier() != null) {
            response.setSupplierId(purchase.getSupplier().getId());
            response.setSupplierName(purchase.getSupplier().getName());
        }
        response.setInvoiceNumber(purchase.getInvoiceNumber());
        response.setRemarks(purchase.getRemarks());
        response.setCreatedAt(purchase.getCreatedAt());
        response.setCreatedBy(purchase.getCreatedBy());

        List<PurchaseItemResponse> purchaseItems = purchase.getPurchaseItems().stream()
                .map(this::toPurchaseItemResponse)
                .collect(Collectors.toList());
        response.setPurchaseItems(purchaseItems);

        return response;
    }

    private PurchaseItemResponse toPurchaseItemResponse(PurchaseItem purchaseItem) {
        if (purchaseItem == null) {
            return null;
        }

        return new PurchaseItemResponse(
                purchaseItem.getId(),
                purchaseItem.getProduct().getId(),
                purchaseItem.getProduct().getName(),
                purchaseItem.getQuantity(),
                purchaseItem.getUnitPrice()
        );
    }
}
