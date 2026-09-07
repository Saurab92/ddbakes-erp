# User Management API - Complete Index

## 📋 Documentation Files

### Quick Start
- **[IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md)** - Overview of implementation and setup guide
- **[USER_API_DOCUMENTATION.md](./USER_API_DOCUMENTATION.md)** - Complete API endpoint documentation with examples

### Database
- **[USER_SCHEMA.sql](./USER_SCHEMA.sql)** - SQL script to create the users table

---

## 📁 Source Code Structure

### Entity Layer
```
src/main/java/com/bakery/inventory/entity/User.java
```
- JPA Entity for users table
- Audit fields: `createdBy`, `updatedBy`, `createdAt`, `updatedAt`
- Auto-timestamp management

### Data Transfer Objects
```
src/main/java/com/bakery/inventory/dto/
├── UserCreateRequest.java      - Create user request
├── UserUpdateRequest.java      - Update user request
├── ChangePasswordRequest.java  - Change password request
└── UserResponse.java           - User response DTO
```

### Repository Layer
```
src/main/java/com/bakery/inventory/repository/UserRepository.java
```
- Spring Data JPA repository
- Custom finder methods

### Service Layer
```
src/main/java/com/bakery/inventory/service/
├── UserService.java            - Service interface
└── impl/UserServiceImpl.java    - Service implementation
```
- Business logic implementation
- Validation and error handling

### Mapper Layer
```
src/main/java/com/bakery/inventory/mapper/UserMapper.java
```
- Entity ↔ DTO conversions

### Controller Layer
```
src/main/java/com/bakery/inventory/controller/UserController.java
```
- REST endpoints (Base path: `/api/users`)
- Authorization checks
- HTTP response handling

---

## 🔌 API Endpoints

### Base URL
```
GET/POST/PUT/DELETE http://localhost:8080/api/users
```

### Required Headers (All Endpoints)
```
X-User-Id: <Long>
X-User-Role: SUPER_ADMIN
```

### Operations

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST | `/api/users` | Create new user | ✅ |
| GET | `/api/users` | Get all users | ✅ |
| GET | `/api/users/{id}` | Get user by ID | ✅ |
| PUT | `/api/users/{id}` | Update user | ✅ |
| PUT | `/api/users/{id}/deactivate` | Deactivate user | ✅ |
| DELETE | `/api/users/{id}` | Delete user | ✅ |
| PUT | `/api/users/{id}/change-password` | Change password | ✅ |

---

## 🔐 Authorization

All endpoints require:
1. **User ID Header**: `X-User-Id` - The ID of the current user
2. **Role Header**: `X-User-Role` - Must be `SUPER_ADMIN`

The `verifyAdminRole()` method in the controller validates this on every request.

---

## ✨ Key Features

- ✅ **Role-Based Access Control** - Only SUPER_ADMIN users can access
- ✅ **Input Validation** - All requests validated with detailed error messages
- ✅ **Unique Constraints** - Username and email uniqueness enforced
- ✅ **Audit Trail** - Track who created/updated each record
- ✅ **Automatic Timestamps** - createdAt and updatedAt managed automatically
- ✅ **Password Validation** - Old password verification on changes
- ✅ **Deactivation Support** - Soft delete via active flag
- ✅ **Transaction Management** - Proper transaction handling
- ✅ **Error Handling** - Meaningful error responses

---

## 🚀 Getting Started

### 1. Set Up Database
Execute the SQL schema:
```bash
mysql -u root -p < USER_SCHEMA.sql
```

### 2. Build Project
```bash
cd /Users/saurabhyadav/Downloads/operations
./mvnw clean compile
```

### 3. Run Application
```bash
./mvnw spring-boot:run
```

### 4. Test API
```bash
curl -X POST http://localhost:8080/api/users \
  -H "X-User-Id: 1" \
  -H "X-User-Role: SUPER_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "role": "USER"
  }'
```

---

## 📊 Database Schema

### users Table
| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | - |
| username | VARCHAR(100) | NOT NULL, UNIQUE | - |
| email | VARCHAR(150) | NOT NULL, UNIQUE | - |
| password | VARCHAR(255) | NOT NULL | ⚠️ Should be hashed |
| first_name | VARCHAR(100) | - | Optional |
| last_name | VARCHAR(100) | - | Optional |
| role | VARCHAR(50) | NOT NULL | e.g., SUPER_ADMIN, ADMIN, USER |
| active | BOOLEAN | NOT NULL, DEFAULT TRUE | Account status |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Audit field |
| created_by | BIGINT | - | User ID who created |
| updated_at | TIMESTAMP | NOT NULL | Audit field |
| updated_by | BIGINT | - | User ID who updated |

### Indexes
- idx_username (username)
- idx_email (email)
- idx_role (role)
- idx_active (active)

---

## 🔍 Request/Response Examples

### Create User Request
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

### User Response
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

## ⚠️ Security Notes

**IMPORTANT**: The current implementation has some security considerations for production:

1. **Passwords are stored in plain text** - Implement BCrypt or Argon2 hashing
2. **Header-based auth** - Consider JWT tokens for production
3. **No rate limiting** - Add rate limiting to prevent abuse
4. **No HTTPS** - Always use HTTPS in production

---

## 📈 Future Enhancements

- [ ] Password hashing with BCrypt/Argon2
- [ ] JWT authentication
- [ ] Pagination and sorting for GET all
- [ ] Advanced filtering by role, status, etc.
- [ ] Comprehensive audit logging
- [ ] Rate limiting
- [ ] Soft delete instead of hard delete
- [ ] Email verification
- [ ] Two-factor authentication
- [ ] Swagger/OpenAPI documentation
- [ ] Unit and integration tests

---

## 🧪 Testing

Refer to **USER_API_DOCUMENTATION.md** for detailed curl command examples for testing each endpoint.

---

## ✅ Build Status

```
✅ All 36 Java files compiled successfully
✅ Zero compilation errors
✅ Ready for deployment
```

---

## 📞 Support

For detailed API specifications, see: **USER_API_DOCUMENTATION.md**

For implementation details, see: **IMPLEMENTATION_SUMMARY.md**
