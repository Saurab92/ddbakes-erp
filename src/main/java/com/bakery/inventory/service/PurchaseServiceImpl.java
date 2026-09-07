package com.bakery.inventory.service;

import com.bakery.inventory.dto.PurchaseCreateRequest;
import com.bakery.inventory.dto.PurchaseItemRequest;
import com.bakery.inventory.dto.PurchaseResponse;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Purchase;
import com.bakery.inventory.entity.PurchaseItem;
import com.bakery.inventory.entity.Stock;
import com.bakery.inventory.entity.Supplier;
import com.bakery.inventory.mapper.PurchaseMapper;
import com.bakery.inventory.repository.ProductRepository;
import com.bakery.inventory.repository.PurchaseRepository;
import com.bakery.inventory.repository.StockRepository;
import com.bakery.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, ProductRepository productRepository,
                                StockRepository stockRepository, SupplierRepository supplierRepository,
                                PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public PurchaseResponse createPurchase(PurchaseCreateRequest request, Long userId) {
        // Step 1: Validate all products exist before making any changes
        for (PurchaseItemRequest itemRequest : request.getPurchaseItems()) {
            validateProduct(itemRequest.getProductId());
        }
        Supplier supplier = validateSupplier(request.getSupplierId());

        // Step 2: Create Purchase entity
        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setSupplier(supplier);
        purchase.setInvoiceNumber(request.getInvoiceNumber());
        purchase.setRemarks(request.getRemarks());
        purchase.setCreatedBy(userId);
        purchase.setUpdatedBy(userId);

        // Step 3: Create PurchaseItems and update Stock (within same transaction)
        List<PurchaseItem> purchaseItems = request.getPurchaseItems().stream()
                .map(itemRequest -> createPurchaseItemAndUpdateStock(purchase, itemRequest, userId))
                .collect(Collectors.toList());

        purchase.setPurchaseItems(purchaseItems);

        // Step 4: Save purchase and return response
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseResponse(savedPurchase);
    }

    @Override
    public PurchaseResponse updatePurchase(Long id, PurchaseCreateRequest request, Long userId) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found with ID: " + id));

        // Step 1: Validate all new products exist before making any changes
        for (PurchaseItemRequest itemRequest : request.getPurchaseItems()) {
            validateProduct(itemRequest.getProductId());
        }
        Supplier supplier = validateSupplier(request.getSupplierId());

        // Step 2: Reverse the stock impact of the existing purchase items
        for (PurchaseItem existingItem : purchase.getPurchaseItems()) {
            reverseStockForItem(existingItem, userId);
        }

        // Step 3: Update purchase header fields
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setSupplier(supplier);
        purchase.setInvoiceNumber(request.getInvoiceNumber());
        purchase.setRemarks(request.getRemarks());
        purchase.setUpdatedBy(userId);

        // Step 4: Replace purchase items and apply new stock impact
        // (orphanRemoval on purchase.purchaseItems deletes the old rows)
        purchase.getPurchaseItems().clear();
        List<PurchaseItem> newItems = request.getPurchaseItems().stream()
                .map(itemRequest -> createPurchaseItemAndUpdateStock(purchase, itemRequest, userId))
                .collect(Collectors.toList());
        purchase.getPurchaseItems().addAll(newItems);

        Purchase savedPurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseResponse(savedPurchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll();
        return purchases.stream()
                .map(purchaseMapper::toPurchaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found with ID: " + id));
        return purchaseMapper.toPurchaseResponse(purchase);
    }

    private void validateProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    }

    private Supplier validateSupplier(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + supplierId));
    }

    private PurchaseItem createPurchaseItemAndUpdateStock(Purchase purchase, PurchaseItemRequest itemRequest, Long userId) {
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + itemRequest.getProductId()));

        // Create PurchaseItem
        PurchaseItem purchaseItem = new PurchaseItem(purchase, product, itemRequest.getQuantity(), itemRequest.getUnitPrice());
        purchaseItem.setCreatedBy(userId);
        purchaseItem.setUpdatedBy(userId);

        // Update Stock - add quantity, creating the stock row if it doesn't exist yet
        Stock stock = stockRepository.findByProductId(product.getId())
                .orElseGet(() -> new Stock(product, BigDecimal.ZERO));

        BigDecimal newQuantity = stock.getQuantity().add(itemRequest.getQuantity());
        stock.setQuantity(newQuantity);
        if (stock.getId() == null) {
            stock.setCreatedBy(userId);
        }
        stock.setUpdatedBy(userId);
        stockRepository.save(stock);

        return purchaseItem;
    }

    private void reverseStockForItem(PurchaseItem existingItem, Long userId) {
        stockRepository.findByProductId(existingItem.getProduct().getId()).ifPresent(stock -> {
            BigDecimal newQuantity = stock.getQuantity().subtract(existingItem.getQuantity());
            stock.setQuantity(newQuantity);
            stock.setUpdatedBy(userId);
            stockRepository.save(stock);
        });
    }
}
