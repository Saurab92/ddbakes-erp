package com.bakery.inventory.service;

import com.bakery.inventory.dto.IssueCreateRequest;
import com.bakery.inventory.dto.IssueItemRequest;
import com.bakery.inventory.dto.IssueResponse;
import com.bakery.inventory.entity.Department;
import com.bakery.inventory.entity.Issue;
import com.bakery.inventory.entity.IssueItem;
import com.bakery.inventory.entity.Person;
import com.bakery.inventory.entity.Product;
import com.bakery.inventory.entity.Stock;
import com.bakery.inventory.mapper.IssueMapper;
import com.bakery.inventory.repository.DepartmentRepository;
import com.bakery.inventory.repository.IssueRepository;
import com.bakery.inventory.repository.PersonRepository;
import com.bakery.inventory.repository.ProductRepository;
import com.bakery.inventory.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final DepartmentRepository departmentRepository;
    private final PersonRepository personRepository;
    private final IssueMapper issueMapper;

    public IssueServiceImpl(IssueRepository issueRepository, ProductRepository productRepository,
                          StockRepository stockRepository, DepartmentRepository departmentRepository,
                          PersonRepository personRepository, IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.departmentRepository = departmentRepository;
        this.personRepository = personRepository;
        this.issueMapper = issueMapper;
    }

    @Override
    public IssueResponse createIssue(IssueCreateRequest request, Long userId) {
        // Step 1: Validate department and person exist
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + request.getDepartmentId()));
        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new IllegalArgumentException("Person not found with ID: " + request.getPersonId()));

        // Step 2: Validate all products exist and sufficient stock is available
        for (IssueItemRequest itemRequest : request.getIssueItems()) {
            validateProductAndStock(itemRequest.getProductId(), itemRequest.getQuantity());
        }

        // Step 3: Create Issue entity
        Issue issue = new Issue();
        issue.setIssueDate(request.getIssueDate());
        issue.setDepartment(department);
        issue.setPerson(person);
        issue.setReason(request.getReason());
        issue.setRemarks(request.getRemarks());
        issue.setCreatedBy(userId);
        issue.setUpdatedBy(userId);

        // Step 4: Create IssueItems and update Stock (within same transaction)
        List<IssueItem> issueItems = request.getIssueItems().stream()
                .map(itemRequest -> createIssueItemAndUpdateStock(issue, itemRequest, userId))
                .collect(Collectors.toList());

        issue.setIssueItems(issueItems);

        // Step 5: Save issue and return response
        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toIssueResponse(savedIssue);
    }

    @Override
    public IssueResponse updateIssue(Long id, IssueCreateRequest request, Long userId) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with ID: " + id));

        // Step 1: Validate department and person exist
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + request.getDepartmentId()));
        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new IllegalArgumentException("Person not found with ID: " + request.getPersonId()));

        // Step 2: Reverse the stock impact of the existing issue items before validating the
        // new items, so that re-issuing the same/similar quantities doesn't spuriously fail
        // due to stock still being deducted for the old items.
        for (IssueItem existingItem : issue.getIssueItems()) {
            reverseStockForItem(existingItem, userId);
        }

        // Step 3: Validate all new products exist and sufficient stock is available
        for (IssueItemRequest itemRequest : request.getIssueItems()) {
            validateProductAndStock(itemRequest.getProductId(), itemRequest.getQuantity());
        }

        // Step 4: Update issue header fields
        issue.setIssueDate(request.getIssueDate());
        issue.setDepartment(department);
        issue.setPerson(person);
        issue.setReason(request.getReason());
        issue.setRemarks(request.getRemarks());
        issue.setUpdatedBy(userId);

        // Step 5: Replace issue items and apply new stock impact
        // (orphanRemoval on issue.issueItems deletes the old rows)
        issue.getIssueItems().clear();
        List<IssueItem> newItems = request.getIssueItems().stream()
                .map(itemRequest -> createIssueItemAndUpdateStock(issue, itemRequest, userId))
                .collect(Collectors.toList());
        issue.getIssueItems().addAll(newItems);

        Issue savedIssue = issueRepository.save(issue);
        return issueMapper.toIssueResponse(savedIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueResponse> getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        return issues.stream()
                .map(issueMapper::toIssueResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IssueResponse getIssueById(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with ID: " + id));
        return issueMapper.toIssueResponse(issue);
    }

    private void validateProductAndStock(Long productId, BigDecimal quantity) {
        // Validate product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (!product.getActive()) {
            throw new IllegalArgumentException("Product is inactive: " + product.getName());
        }

        // Validate stock exists and is sufficient
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product: " + product.getName()));

        if (stock.getQuantity().compareTo(quantity) < 0) {
            throw new IllegalArgumentException(
                    String.format("Insufficient stock for product '%s'. Available: %s, Requested: %s",
                            product.getName(), stock.getQuantity(), quantity)
            );
        }
    }

    private IssueItem createIssueItemAndUpdateStock(Issue issue, IssueItemRequest itemRequest, Long userId) {
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + itemRequest.getProductId()));

        // Create IssueItem
        IssueItem issueItem = new IssueItem(issue, product, itemRequest.getQuantity());
        issueItem.setCreatedBy(userId);
        issueItem.setUpdatedBy(userId);

        // Update Stock - deduct quantity
        Stock stock = stockRepository.findByProductId(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for product: " + product.getName()));

        BigDecimal newQuantity = stock.getQuantity().subtract(itemRequest.getQuantity());
        stock.setQuantity(newQuantity);
        stock.setUpdatedBy(userId);
        stockRepository.save(stock);

        return issueItem;
    }

    private void reverseStockForItem(IssueItem existingItem, Long userId) {
        stockRepository.findByProductId(existingItem.getProduct().getId()).ifPresent(stock -> {
            BigDecimal newQuantity = stock.getQuantity().add(existingItem.getQuantity());
            stock.setQuantity(newQuantity);
            stock.setUpdatedBy(userId);
            stockRepository.save(stock);
        });
    }
}
