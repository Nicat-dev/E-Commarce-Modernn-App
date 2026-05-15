# E-Commerce Modern App - API Documentation

## Base URL
`http://localhost:8081/api`

## Authentication
All endpoints except `/auth/*` and `GET /products` require JWT authentication via Bearer token in Authorization header:
```
Authorization: Bearer <jwt_token>
```

---

## Authentication Endpoints

### 1. Login
**POST** `/auth/login`

**Description:** Authenticate user and receive JWT token

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "timestamp": 1714859799157
}
```

---

### 2. Register
**POST** `/auth/register`

**Description:** Register a new user

**Request Body:**
```json
{
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com",
  "username": "johndoe",
  "password": "password123",
  "isActive": true
}
```

**Response (201 Created):**
```json
{
  "message": "User registered successfully",
  "success": true
}
```

---

## User Endpoints

### 1. Create User (Admin only)
**POST** `/users`

**Request Body:**
```json
{
  "name": "Jane Smith",
  "phone": "+1987654321",
  "email": "jane@example.com",
  "username": "janesmith",
  "password": "password456",
  "isActive": true
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "name": "Jane Smith",
  "phone": "+1987654321",
  "email": "jane@example.com",
  "username": "janesmith",
  "isActive": true,
  "purchaseOrders": [],
  "createdAt": "2026-05-04T20:56:39.000Z",
  "updatedAt": "2026-05-04T20:56:39.000Z"
}
```

---

### 2. Get User (User role)
**GET** `/users/{id}`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com",
  "username": "johndoe",
  "isActive": true,
  "purchaseOrders": [
    {
      "id": 1,
      "orderCode": "ORD-2026-001",
      "userId": 1,
      "items": [],
      "createdAt": "2026-05-04T20:56:39.000Z",
      "updatedAt": "2026-05-04T20:56:39.000Z"
    }
  ],
  "createdAt": "2026-05-04T20:56:39.000Z",
  "updatedAt": "2026-05-04T20:56:39.000Z"
}
```

---

### 3. Update User (User role)
**PUT** `/users/{id}`

**Request Body:**
```json
{
  "name": "John Updated",
  "phone": "+1234567890",
  "email": "john.updated@example.com",
  "username": "johndoe",
  "password": "newpassword123",
  "isActive": true
}
```

**Response (200 OK):** Same as Get User response

---

### 4. Delete User (Admin only)
**DELETE** `/users/{id}`

**Response (204 No Content)**

---

## Product Endpoints

### 1. Get All Products (Public)
**GET** `/products`

**Response (200 OK):**
```json
[]
```

---

### 2. Create Product (Admin only)
**POST** `/products`

**Request Body:**
```json
{
  "name": "Laptop",
  "description": "High-performance gaming laptop",
  "price": 1299.99
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance gaming laptop",
  "price": 1299.99,
  "createdAt": "2026-05-04T20:56:39.000Z",
  "updatedAt": "2026-05-04T20:56:39.000Z"
}
```

---

### 3. Get Product (Public)
**GET** `/products/{id}`

**Response (200 OK):** Same as Create Product response

---

### 4. Update Product (Admin only)
**PUT** `/products/{id}`

**Request Body:**
```json
{
  "name": "Laptop Pro",
  "description": "Updated description",
  "price": 1499.99
}
```

**Response (200 OK):** Same as Create Product response

---

### 5. Delete Product (Admin only)
**DELETE** `/products/{id}`

**Response (204 No Content)**

---

## Order Endpoints

### 1. Create Order (User role)
**POST** `/purchaseOrders`

**Request Body:**
```json
{
  "orderCode": "ORD-2026-001",
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "orderCode": "ORD-2026-001",
  "userId": 1,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "productPrice": 1299.99,
      "quantity": 2,
      "createdAt": "2026-05-04T20:56:39.000Z",
      "updatedAt": "2026-05-04T20:56:39.000Z"
    }
  ],
  "createdAt": "2026-05-04T20:56:39.000Z",
  "updatedAt": "2026-05-04T20:56:39.000Z"
}
```

---

### 2. Get Order (User role)
**GET** `/purchaseOrders/{id}`

**Response (200 OK):** Same as Create Order response

---

### 3. Update Order (User role)
**PUT** `/purchaseOrders/{id}`

**Request Body:** Same as Create Order request

**Response (200 OK):** Same as Create Order response

---

### 4. Delete Order (User role)
**DELETE** `/purchaseOrders/{id}`

**Response (204 No Content)**

---

## Order Item Endpoints

### 1. Create Order Item (User role)
**POST** `/purchaseOrder-items`

**Request Body:**
```json
{
  "productId": 1,
  "quantity": 3
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Laptop",
  "productPrice": 1299.99,
  "quantity": 3,
  "createdAt": "2026-05-04T20:56:39.000Z",
  "updatedAt": "2026-05-04T20:56:39.000Z"
}
```

---

### 2. Get Order Item (User role)
**GET** `/purchaseOrder-items/{id}`

**Response (200 OK):** Same as Create Order Item response

---

### 3. Update Order Item (User role)
**PUT** `/purchaseOrder-items/{id}`

**Request Body:**
```json
{
  "productId": 1,
  "quantity": 5
}
```

**Response (200 OK):** Same as Create Order Item response

---

### 4. Delete Order Item (User role)
**DELETE** `/purchaseOrder-items/{id}`

**Response (204 No Content)**

---

## Error Responses

### 400 Bad Request (Validation Error)
```json
{
  "errors": {
    "email": "User email must be a valid email address",
    "password": "User password must be at least 6 characters long"
  },
  "success": false,
  "message": "Validation failed"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid token or token expired",
  "timestamp": 1714859799157
}
```

### 403 Forbidden
```json
{
  "error": "Forbidden",
  "message": "Access denied",
  "timestamp": 1714859799157
}
```

### 404 Not Found
```json
{
  "error": "User not found!",
  "status": "USER_NOT_FOUND",
  "success": false
}
```

### 409 Conflict
```json
{
  "error": "Email already exists!",
  "status": "EMAIL_ALREADY_EXISTS",
  "success": false
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "success": false,
  "message": "An unexpected error occurred"
}
```

---

## Request/Response Validation

### User Fields Validation
- `name`: Required, cannot be blank
- `phone`: Required, cannot be blank
- `email`: Required, valid email format
- `username`: Required, cannot be blank, must be unique
- `password`: Required, minimum 6 characters
- `isActive`: Required, boolean

### Product Fields Validation
- `name`: Required, cannot be blank
- `description`: Required, cannot be blank
- `price`: Required, must be positive (> 0)

### Order Fields Validation
- `orderCode`: Required, cannot be blank, must be unique
- `userId`: Required, must reference existing user
- `items`: Required, must contain at least one item, @Valid for nested validation

### OrderItem Fields Validation
- `productId`: Required, must reference existing product
- `quantity`: Required, must be positive (> 0)

---

## Role-Based Access Control

| Endpoint | Method | Public | User | Admin |
|----------|--------|--------|------|-------|
| /auth/login | POST | ✓ | ✓ | ✓ |
| /auth/register | POST | ✓ | ✓ | ✓ |
| /products | GET | ✓ | ✓ | ✓ |
| /products/{id} | GET | ✓ | ✓ | ✓ |
| /products | POST | | | ✓ |
| /products/{id} | PUT | | | ✓ |
| /products/{id} | DELETE | | | ✓ |
| /users | POST | | | ✓ |
| /users/{id} | GET | | ✓ | ✓ |
| /users/{id} | PUT | | ✓ | ✓ |
| /users/{id} | DELETE | | | ✓ |
| /purchaseOrders | POST | | ✓ | ✓ |
| /purchaseOrders/{id} | GET | | ✓ | ✓ |
| /purchaseOrders/{id} | PUT | | ✓ | ✓ |
| /purchaseOrders/{id} | DELETE | | ✓ | ✓ |
| /purchaseOrder-items | POST | | ✓ | ✓ |
| /purchaseOrder-items/{id} | GET | | ✓ | ✓ |
| /purchaseOrder-items/{id} | PUT | | ✓ | ✓ |
| /purchaseOrder-items/{id} | DELETE | | ✓ | ✓ |

---

## Database Schema

### Users Table
- `id` (PK): Long, auto-incremented
- `name`: String
- `phone`: String
- `email`: String (Unique)
- `username`: String (Unique)
- `password`: String (Encrypted)
- `isActive`: Boolean (Default: true)
- `createdAt`: Timestamp (Auto)
- `updatedAt`: Timestamp (Auto)
- Relationship: OneToMany with Orders

### Products Table
- `id` (PK): Long, auto-incremented
- `name`: String
- `description`: String
- `price`: Double
- `createdAt`: Timestamp (Auto)
- `updatedAt`: Timestamp (Auto)
- Relationship: OneToMany with OrderItems

### Orders Table
- `id` (PK): Long, auto-incremented
- `orderCode`: String (Unique)
- `user_id` (FK): Long (References Users)
- `createdAt`: Timestamp (Auto)
- `updatedAt`: Timestamp (Auto)
- Relationships: ManyToOne with Users, OneToMany with OrderItems

### OrderItems Table
- `id` (PK): Long, auto-incremented
- `order_id` (FK): Long (References Orders)
- `product_id` (FK): Long (References Products)
- `quantity`: Integer
- `createdAt`: Timestamp (Auto)
- `updatedAt`: Timestamp (Auto)
- Relationships: ManyToOne with Orders, ManyToOne with Products

---

## JWT Token Format
```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "johndoe",
  "iat": 1714859799,
  "exp": 1714946199
}

Signature:
HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

Token expiration: 24 hours

