# Issue Items API - Documentation

## Overview
The Issue Items API allows you to issue (remove) items from your inventory stock. When you create an issue, the system:
1. Creates an issue record for tracking purposes
2. Automatically deducts the quantity from the product's stock
3. Validates that sufficient stock exists before allowing the issue
4. Maintains an audit trail (who created the issue, when, etc.)

---

## Base URL
```
http://localhost:8080/api/issues
```

---

## Endpoints

### 1. Create Issue (Issue Items and Deduct from Stock)

**Endpoint**: `POST /api/issues`

**Description**: Create a new issue and automatically deduct items from stock.

**Request Headers**:
```
Content-Type: application/json
X-User-Id: <user-id> (optional, defaults to 1 for testing)
```

**Request Body**:
```json
{
  "issueDate": "2026-09-06",
  "reason": "Sample issuance for use",
  "remarks": "Optional remarks about the issue",
  "issueItems": [
    {
      "productId": 1,
      "quantity": 5
    },
    {
      "productId": 2,
      "quantity": 10.5
    }
  ]
}
```

**Request Fields**:
- `issueDate` (LocalDate, required): Date when items are being issued (format: YYYY-MM-DD)
- `reason` (String, optional): Reason for issuing items
- `remarks` (String, optional): Additional remarks
- `issueItems` (Array, required): List of items to issue
  - `productId` (Long, required): ID of the product to issue
  - `quantity` (BigDecimal, required): Quantity to issue (must be > 0)

**Response** (HTTP 201 Created):
```json
{
  "id": 1,
  "issueDate": "2026-09-06",
  "reason": "Sample issuance for use",
  "remarks": "Optional remarks about the issue",
  "createdAt": "2026-09-06T03:24:15.123",
  "createdBy": 1,
  "issueItems": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Flour",
      "quantity": 5
    },
    {
      "id": 2,
      "productId": 2,
      "productName": "Sugar",
      "quantity": 10.5
    }
  ]
}
```

**Error Responses**:

- **HTTP 400 Bad Request** - Validation errors:
```json
{
  "error": "Validation Error",
  "message": "Insufficient stock for product 'Flour'. Available: 3, Requested: 5"
}
```

- **HTTP 400 Bad Request** - Product not found:
```json
{
  "error": "Validation Error",
  "message": "Product not found with ID: 999"
}
```

- **HTTP 400 Bad Request** - Inactive product:
```json
{
  "error": "Validation Error",
  "message": "Product is inactive: Flour"
}
```

- **HTTP 500 Internal Server Error**:
```json
{
  "error": "Error",
  "message": "Failed to create issue: <error-details>"
}
```

---

### 2. Get All Issues

**Endpoint**: `GET /api/issues`

**Description**: Retrieve all issues that have been created.

**Response** (HTTP 200 OK):
```json
[
  {
    "id": 1,
    "issueDate": "2026-09-06",
    "reason": "Sample issuance for use",
    "remarks": "Optional remarks about the issue",
    "createdAt": "2026-09-06T03:24:15.123",
    "createdBy": 1,
    "issueItems": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Flour",
        "quantity": 5
      }
    ]
  }
]
```

---

### 3. Get Issue by ID

**Endpoint**: `GET /api/issues/{id}`

**Description**: Retrieve a specific issue by its ID.

**Path Parameters**:
- `id` (Long, required): The issue ID

**Response** (HTTP 200 OK):
```json
{
  "id": 1,
  "issueDate": "2026-09-06",
  "reason": "Sample issuance for use",
  "remarks": "Optional remarks about the issue",
  "createdAt": "2026-09-06T03:24:15.123",
  "createdBy": 1,
  "issueItems": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Flour",
      "quantity": 5
    }
  ]
}
```

**Error Response** (HTTP 404 Not Found):
```json
{
  "error": "Not Found",
  "message": "Issue not found with ID: 999"
}
```

---

## cURL Examples

### Create an Issue

```bash
curl -X POST http://localhost:8080/api/issues \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{
    "issueDate": "2026-09-06",
    "reason": "Production use",
    "remarks": "Daily production batch",
    "issueItems": [
      {
        "productId": 1,
        "quantity": 5
      },
      {
        "productId": 2,
        "quantity": 10.5
      }
    ]
  }'
```

### Get All Issues

```bash
curl -X GET http://localhost:8080/api/issues \
  -H "Content-Type: application/json"
```

### Get Issue by ID

```bash
curl -X GET http://localhost:8080/api/issues/1 \
  -H "Content-Type: application/json"
```

---

## Validation Rules

The API enforces the following validation rules:

1. **Issue Date** - Required and must be a valid date (YYYY-MM-DD format)
2. **Issue Items** - At least one item is required
3. **Product ID** - Must be a valid, existing, and active product
4. **Quantity** - Must be greater than 0
5. **Stock Validation** - The requested quantity must not exceed available stock
   - If stock is insufficient, the entire issue is rejected (nothing is deducted)
   - Example: If Product A has 3 units but you request 5, the issue fails
6. **Stock Existence** - A stock record must exist for the product

---

## Business Logic

### Stock Deduction Flow

When you create an issue:

1. **Validation Phase**:
   - System checks if all products exist
   - System verifies all products are active
   - System verifies all quantities are > 0
   - System checks if sufficient stock exists for each product

2. **Transaction Phase** (Atomic - all or nothing):
   - If validation passes, an Issue record is created
   - For each item in the issue:
     - An IssueItem record is created (links issue to product and quantity)
     - The product's Stock quantity is reduced by the issued quantity
   - If any error occurs, the entire transaction is rolled back

3. **Response Phase**:
   - Complete issue details are returned with all created items

### Important Notes

- ✅ **Atomicity**: If there's an error, NO stock changes are made (all-or-nothing)
- ✅ **Stock Visibility**: Stock is updated immediately after issue creation
- ✅ **Audit Trail**: `createdBy` field tracks who created the issue
- ✅ **Precision**: Quantities support decimal values (e.g., 10.5 units)
- ⚠️ **Negative Stock**: The system prevents creating issues that would result in negative stock

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 201 | Issue created successfully |
| 200 | Request successful |
| 400 | Validation error or bad request |
| 404 | Resource not found |
| 500 | Internal server error |

---

## Integration with Stock

After an issue is created, the Stock table is automatically updated:

**Before Issue**:
```
Product ID: 1 (Flour)
Stock Quantity: 100
```

**After Issue (5 units)**:
```
Product ID: 1 (Flour)
Stock Quantity: 95
```

---

## Error Handling

All errors are returned in a consistent format:

```json
{
  "error": "<Error Type>",
  "message": "<Detailed error message>"
}
```

Common errors:
- **Insufficient stock**: "Insufficient stock for product 'X'. Available: Y, Requested: Z"
- **Product not found**: "Product not found with ID: X"
- **Product inactive**: "Product is inactive: X"
- **Quantity invalid**: "Quantity must be greater than 0"
- **Missing required field**: "<Field> is required"

---

## Database Schema

### Issues Table
```sql
CREATE TABLE issues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_date DATE NOT NULL,
    reason VARCHAR(255),
    remarks VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    INDEX idx_issue_date (issue_date),
    INDEX idx_created_by (created_by)
);
```

### Issue Items Table
```sql
CREATE TABLE issue_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12, 3) NOT NULL,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_issue_item_issue FOREIGN KEY (issue_id) REFERENCES issues(id),
    CONSTRAINT fk_issue_item_product FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_issue_id (issue_id),
    INDEX idx_product_id (product_id)
);
```

---

## Future Enhancements

1. **Issue Status**: Add status field (ISSUED, RETURNED, CANCELLED)
2. **Reverse Issues**: Create ability to reverse/cancel an issue and restore stock
3. **Issue History**: Track modifications to issues
4. **Batch Operations**: Issue items to multiple locations
5. **Reports**: Generate issue reports by date range, reason, etc.
6. **Approval Workflow**: Add approval step for large issues
7. **Notifications**: Send notifications when issues are created
8. **Issue Reasons**: Pre-defined list of issue reasons for categorization

---

## Setup Instructions

### 1. Create Database Tables

Execute the SQL schema:
```bash
mysql -u root -p < ISSUE_SCHEMA.sql
```

Or manually run the SQL commands in `ISSUE_SCHEMA.sql`

### 2. Build the Project

```bash
./mvnw clean compile
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at: `http://localhost:8080/api/issues`

---

## Testing Checklist

- [ ] Create issue with valid data
- [ ] Create issue with insufficient stock (should fail)
- [ ] Create issue with invalid product ID (should fail)
- [ ] Get all issues
- [ ] Get specific issue by ID
- [ ] Verify stock is deducted after issue creation
- [ ] Verify error messages are clear and helpful
- [ ] Test with multiple items in single issue
- [ ] Test with decimal quantities

