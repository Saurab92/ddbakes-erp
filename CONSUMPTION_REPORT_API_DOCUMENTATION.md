# Consumption Report API - Documentation

## Overview
The Consumption Report API provides read-only reports summarizing how stock is consumed, derived entirely
from existing **Issue** / **Issue Item** records. It answers three questions:

1. **Department-wise Consumption** — what each department consumed
2. **Product-wise Consumption** — how much of each product was issued
3. **Daily/Monthly Consumption Trend** — consumption trends over time

All reports support optional filtering by date range, department, and product, and can be returned as
JSON (default) or CSV.

---

## Base URL
```
http://localhost:8080/api/reports/consumption
```

---

## Common Query Parameters

All three endpoints accept the same set of query parameters:

| Parameter      | Type       | Required | Description                                                                 |
|----------------|------------|----------|-------------------------------------------------------------------------------|
| `startDate`    | date (`yyyy-MM-dd`) | No | Inclusive lower bound on issue date. Omit for no lower bound.        |
| `endDate`      | date (`yyyy-MM-dd`) | No | Inclusive upper bound on issue date. Omit for no upper bound.        |
| `departmentId` | Long       | No       | Restrict the report to a single department.                                   |
| `productId`    | Long       | No       | Restrict the report to a single product.                                      |
| `format`       | String     | No       | `json` (default) or `csv`. When `csv`, the response is a downloadable CSV file. |

The `/trend` endpoint additionally accepts:

| Parameter | Type   | Required | Description                                              |
|-----------|--------|----------|-----------------------------------------------------------|
| `groupBy` | String | No       | `DAILY` (default) or `MONTHLY` bucket granularity.         |

---

## Endpoints

### 1. Department-wise Consumption

**Endpoint**: `GET /api/reports/consumption/department-wise`

**Description**: Total quantity issued and number of issues per department.

**Example**:
```
GET /api/reports/consumption/department-wise?startDate=2026-08-01&endDate=2026-08-31
```

**Response** (HTTP 200 OK):
```json
[
  {
    "departmentId": 1,
    "departmentName": "Bakery",
    "totalQuantity": 245.500,
    "issueCount": 18
  },
  {
    "departmentId": 2,
    "departmentName": "Sales",
    "totalQuantity": 60.000,
    "issueCount": 5
  }
]
```

**CSV export**:
```
GET /api/reports/consumption/department-wise?format=csv
```
Returns a `text/csv` file with columns: `departmentId,departmentName,totalQuantity,issueCount`.

---

### 2. Product-wise Consumption

**Endpoint**: `GET /api/reports/consumption/product-wise`

**Description**: Total quantity issued and number of issues per product.

**Example**:
```
GET /api/reports/consumption/product-wise?departmentId=1
```

**Response** (HTTP 200 OK):
```json
[
  {
    "productId": 10,
    "productName": "Refined Flour",
    "unitName": "Kilogram",
    "totalQuantity": 120.000,
    "issueCount": 8
  }
]
```

**CSV export**:
```
GET /api/reports/consumption/product-wise?format=csv
```
Returns a `text/csv` file with columns: `productId,productName,unitName,totalQuantity,issueCount`.

---

### 3. Daily/Monthly Consumption Trend

**Endpoint**: `GET /api/reports/consumption/trend`

**Description**: Total quantity issued and number of issues bucketed by day or month, useful for charting
consumption trends over time.

**Example (daily)**:
```
GET /api/reports/consumption/trend?startDate=2026-08-01&endDate=2026-08-07&groupBy=DAILY
```

**Response** (HTTP 200 OK):
```json
[
  { "period": "2026-08-01", "totalQuantity": 30.000, "issueCount": 3 },
  { "period": "2026-08-02", "totalQuantity": 15.500, "issueCount": 1 }
]
```

**Example (monthly)**:
```
GET /api/reports/consumption/trend?groupBy=MONTHLY
```

**Response** (HTTP 200 OK):
```json
[
  { "period": "2026-07", "totalQuantity": 512.000, "issueCount": 40 },
  { "period": "2026-08", "totalQuantity": 610.250, "issueCount": 52 }
]
```

**CSV export**:
```
GET /api/reports/consumption/trend?groupBy=MONTHLY&format=csv
```
Returns a `text/csv` file with columns: `period,totalQuantity,issueCount`.

---

## Error Responses

| Status | Condition                                         | Body                                                             |
|--------|----------------------------------------------------|-------------------------------------------------------------------|
| 400    | `startDate` is after `endDate`                     | `{ "error": "Validation Error", "message": "startDate must not be after endDate" }` |
| 400    | `groupBy` is not `DAILY` or `MONTHLY`               | `{ "error": "Validation Error", "message": "groupBy must be either DAILY or MONTHLY" }` |
| 500    | Unexpected server error                            | `{ "error": "Error", "message": "Failed to generate ... report: <details>" }` |

---

## Notes
- Reports are computed on-demand from `issues` and `issue_items`; no additional tables are required.
- Quantities reflect the sum of `IssueItem.quantity` for matching rows; `issueCount` counts distinct issues
  (not issue items), so a single issue with multiple product lines still counts once.
- When both `departmentId` and `productId` filters are supplied, all reports are scoped to issues matching
  both filters simultaneously.
