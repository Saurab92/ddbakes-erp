# User Management API Documentation

## Overview
This API provides complete user management functionality with role-based access control. Only users with the `SUPER_ADMIN` role can perform user management operations.

## Base URL
```
/api/users
```

## Authentication
The API requires two headers for authorization:
- `X-User-Id`: The ID of the current authenticated user
- `X-User-Role`: The role of the current authenticated user (must be `SUPER_ADMIN`)

---

## Endpoints

### 1. Create User
**POST** `/api/users`

Creates a new user in the system.

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
Content-Type: application/json
```

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "active": true,
  "createdAt": "2026-09-06T03:06:21+05:30",
  "updatedAt": "2026-09-06T03:06:21+05:30",
  "createdBy": 1,
  "updatedBy": 1
}
```

**Validation:**
- `username`: Required, 3-100 characters, must be unique
- `email`: Required, valid email format, must be unique
- `password`: Required, minimum 6 characters
- `firstName`: Required, 1-100 characters
- `lastName`: Required, 1-100 characters
- `role`: Required

---

### 2. Get All Users
**GET** `/api/users`

Retrieves all users from the system.

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER",
    "active": true,
    "createdAt": "2026-09-06T03:06:21+05:30",
    "updatedAt": "2026-09-06T03:06:21+05:30",
    "createdBy": 1,
    "updatedBy": 1
  },
  {
    "id": 2,
    "username": "jane_smith",
    "email": "jane@example.com",
    "firstName": "Jane",
    "lastName": "Smith",
    "role": "USER",
    "active": true,
    "createdAt": "2026-09-06T03:06:21+05:30",
    "updatedAt": "2026-09-06T03:06:21+05:30",
    "createdBy": 1,
    "updatedBy": 1
  }
]
```

---

### 3. Get User by ID
**GET** `/api/users/{id}`

Retrieves a specific user by ID.

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
```

**Path Parameters:**
- `id`: User ID (Long)

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "active": true,
  "createdAt": "2026-09-06T03:06:21+05:30",
  "updatedAt": "2026-09-06T03:06:21+05:30",
  "createdBy": 1,
  "updatedBy": 1
}
```

---

### 4. Update User
**PUT** `/api/users/{id}`

Updates an existing user's information.

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
Content-Type: application/json
```

**Path Parameters:**
- `id`: User ID (Long)

**Request Body (all fields optional):**
```json
{
  "email": "newemail@example.com",
  "firstName": "Jonathan",
  "lastName": "Doe",
  "role": "ADMIN"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "newemail@example.com",
  "firstName": "Jonathan",
  "lastName": "Doe",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-09-06T03:06:21+05:30",
  "updatedAt": "2026-09-06T03:06:30+05:30",
  "createdBy": 1,
  "updatedBy": 1
}
```

**Notes:**
- Only provided fields will be updated
- Email must be unique across the system
- Username cannot be changed through this endpoint

---

### 5. Deactivate User
**PUT** `/api/users/{id}/deactivate`

Deactivates a user account (sets `active` to `false`).

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
```

**Path Parameters:**
- `id`: User ID (Long)

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "newemail@example.com",
  "firstName": "Jonathan",
  "lastName": "Doe",
  "role": "ADMIN",
  "active": false,
  "createdAt": "2026-09-06T03:06:21+05:30",
  "updatedAt": "2026-09-06T03:06:35+05:30",
  "createdBy": 1,
  "updatedBy": 1
}
```

---

### 6. Delete User
**DELETE** `/api/users/{id}`

Permanently deletes a user from the system.

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
```

**Path Parameters:**
- `id`: User ID (Long)

**Response (204 No Content)**

---

### 7. Change User Password
**PUT** `/api/users/{id}/change-password`

Changes the password for a specific user.

**Request Headers:**
```
X-User-Id: 1
X-User-Role: SUPER_ADMIN
Content-Type: application/json
```

**Path Parameters:**
- `id`: User ID (Long)

**Request Body:**
```json
{
  "oldPassword": "password123",
  "newPassword": "newpassword456"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "newemail@example.com",
  "firstName": "Jonathan",
  "lastName": "Doe",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-09-06T03:06:21+05:30",
  "updatedAt": "2026-09-06T03:06:40+05:30",
  "createdBy": 1,
  "updatedBy": 1
}
```

**Validation:**
- `oldPassword`: Required, must match current password
- `newPassword`: Required, minimum 6 characters

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2026-09-06T03:06:21+05:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists: john_doe",
  "path": "/api/users"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2026-09-06T03:06:21+05:30",
  "status": 401,
  "error": "Unauthorized",
  "message": "Only SUPER_ADMIN role can perform this operation",
  "path": "/api/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2026-09-06T03:06:21+05:30",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999",
  "path": "/api/users/999"
}
```

---

## HTTP Status Codes
- `200 OK`: Successful GET, PUT request
- `201 Created`: Successful POST request
- `204 No Content`: Successful DELETE request
- `400 Bad Request`: Validation error or business rule violation
- `401 Unauthorized`: Missing or invalid authorization
- `404 Not Found`: Resource not found

---

## Security Notes
1. Only `SUPER_ADMIN` users can access these endpoints
2. Authorization is checked via headers `X-User-Id` and `X-User-Role`
3. Password changes are validated against the old password
4. Username and email uniqueness is enforced at the database level
5. User creation/update operations track who made the change via `createdBy` and `updatedBy` fields

---

## Database Schema
The users table includes the following columns:
- `id`: Primary key (auto-increment)
- `username`: Unique username
- `email`: Unique email address
- `password`: Encrypted password (should be hashed in production)
- `first_name`: User's first name
- `last_name`: User's last name
- `role`: User role (e.g., SUPER_ADMIN, ADMIN, USER)
- `active`: Account status (true/false)
- `created_at`: Timestamp of creation
- `updated_at`: Timestamp of last update
- `created_by`: ID of user who created this record
- `updated_by`: ID of user who last updated this record

---

## Example curl Commands

### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER"
  }'
```

### Get All Users
```bash
curl -X GET http://localhost:8080/api/users \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN"
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jonathan",
    "lastName": "Doe",
    "role": "ADMIN"
  }'
```

### Deactivate User
```bash
curl -X PUT http://localhost:8080/api/users/1/deactivate \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN"
```

### Change Password
```bash
curl -X PUT http://localhost:8080/api/users/1/change-password \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "password123",
    "newPassword": "newpassword456"
  }'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN"
```
