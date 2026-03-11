# Spring Security Quick Reference Guide

## 📋 File Structure

```
E-Commarce-Modern-App/
├── src/main/java/com/project/ecommarcemodernapp/
│   ├── controller/
│   │   └── AuthenticationController.java (NEW)
│   ├── dto/
│   │   ├── request/
│   │   │   ├── LoginRequest.java (NEW)
│   │   │   ├── UserRequest.java (UPDATED)
│   │   │   └── ...other dtos
│   │   └── response/
│   │       ├── LoginResponse.java (NEW)
│   │       └── MessageResponse.java (NEW)
│   ├── security/
│   │   ├── SecurityConfig.java (UPDATED)
│   │   ├── JwtProvider.java (NEW)
│   │   ├── CustomUserDetailsService.java (NEW)
│   │   └── JwtAuthenticationFilter.java (NEW)
│   ├── service/
│   │   ├── AuthenticationService.java (NEW)
│   │   ├── UserService.java
│   │   └── impl/
│   │       ├── AuthenticationServiceImpl.java (NEW)
│   │       ├── UserServiceImpl.java (UPDATED)
│   │       └── ...other services
│   └── repository/
│       └── UserRepository.java (UPDATED)
├── build.gradle (UPDATED - added JWT deps)
└── src/main/resources/
    ├── application.properties (UPDATED)
    └── messages.properties (UPDATED)
```

## 🔐 Authentication Flow

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       │ 1. POST /api/auth/register or login
       │    {username, password, ...}
       ▼
┌──────────────────────────┐
│ AuthenticationController │
└──────┬───────────────────┘
       │
       │ 2. Delegate to AuthenticationService
       │
       ▼
┌────────────────────────────────┐
│ AuthenticationServiceImpl       │
│  ├─ AuthenticationManager      │
│  ├─ PasswordEncoder (BCrypt)   │
│  └─ JwtProvider               │
└──────┬─────────────────────────┘
       │
       │ 3. Generate JWT Token
       │    (Username + Expiry + Signature)
       │
       ▼
┌──────────────────────┐
│  Token Response      │
│  {token, type, ...}  │
└──────────────────────┘
       │
       │ 4. Store token on client
       │    (localStorage/sessionStorage)
       │
       ▼
┌─────────────┐
│   Client    │ Now authenticated
└─────────────┘
```

## 🛡️ Request Authorization Flow

```
┌──────────────────────────────────┐
│ Authenticated Request             │
│ Authorization: Bearer <JWT_TOKEN> │
└────────┬─────────────────────────┘
         │
         │ 1. JwtAuthenticationFilter intercepts
         │    request before controller
         │
         ▼
┌────────────────────────────────┐
│ JwtAuthenticationFilter        │
│ 1. Extract token from header   │
│ 2. Validate token signature    │
│ 3. Check token expiration      │
└────────┬───────────────────────┘
         │
         │ 4. If valid, get username from token
         │
         ▼
┌─────────────────────────────┐
│ CustomUserDetailsService    │
│ Load user from database     │
│ Check if user is active     │
└────────┬────────────────────┘
         │
         │ 5. Create authentication object
         │    Set in SecurityContext
         │
         ▼
┌─────────────────────┐
│ SecurityContext     │ User is authenticated
│ ├─ User details     │ for this request
│ ├─ Authorities      │
│ └─ Authentication   │
└────────┬────────────┘
         │
         │ 6. Check authorization rules
         │    (roles, permissions)
         │
         ▼
┌──────────────────┐
│ Controller       │ Execute business logic
│ Method handler   │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Response         │
└──────────────────┘
```

## 🔑 Key Components

| Component | Purpose | Location |
|-----------|---------|----------|
| **JwtProvider** | Generate & validate JWT tokens | `security/JwtProvider.java` |
| **CustomUserDetailsService** | Load user from DB by username | `security/CustomUserDetailsService.java` |
| **JwtAuthenticationFilter** | Intercept requests & validate tokens | `security/JwtAuthenticationFilter.java` |
| **SecurityConfig** | Configure Spring Security | `security/SecurityConfig.java` |
| **AuthenticationService** | Business logic for auth | `service/AuthenticationService.java` |
| **AuthenticationController** | REST endpoints for login/register | `controller/AuthenticationController.java` |

## 📡 API Endpoints

### Public Endpoints (No Token Required)
```
POST   /api/auth/login
POST   /api/auth/register
GET    /api/products
```

### Protected Endpoints (Token Required)
```
GET    /api/orders
POST   /api/orders
GET    /api/orders/{id}
PUT    /api/orders/{id}
DELETE /api/orders/{id}

GET    /api/order-items/{id}
PUT    /api/order-items/{id}
DELETE /api/order-items/{id}

GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Admin-Only Endpoints (Token + ROLE_ADMIN Required)
```
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

## 💡 Usage Examples

### 1️⃣ Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phone": "+1234567890",
    "email": "john@example.com",
    "username": "johndoe",
    "password": "password123",
    "isActive": true
  }'

# Response:
{
  "message": "User registered successfully",
  "success": true
}
```

### 2️⃣ Login & Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123"
  }'

# Response:
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}
```

### 3️⃣ Use Token in Requests
```bash
# Header format: Authorization: Bearer <token>
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."

# JavaScript example:
fetch('/api/orders', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})

# Axios example:
axios.get('/api/orders', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
```

## ⚙️ Configuration

### application.properties
```properties
# JWT Configuration
app.jwt.secret=mySecureSecretKeyForJWTTokenGenerationAndValidationPurposesOnly123456789
app.jwt.expiration=86400000  # 24 hours in milliseconds

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging
logging.level.root=INFO
logging.level.com.project.ecommarcemodernapp=DEBUG
```

## 🔒 Security Features

| Feature | Implementation |
|---------|-----------------|
| **Password Hashing** | BCrypt with strength 10 |
| **Token Signing** | HMAC-SHA512 algorithm |
| **Token Expiration** | 24 hours (configurable) |
| **Email Uniqueness** | Database constraint + service validation |
| **Username Uniqueness** | Database constraint + service validation |
| **User Activation** | `isActive` field checked on login |
| **CORS Support** | localhost:3000 & localhost:4200 |
| **CSRF Protection** | Disabled (stateless JWT API) |
| **Session Management** | STATELESS (no server-side sessions) |

## 📝 Token Structure

```
Header:
{
  "alg": "HS512",
  "typ": "JWT"
}

Payload:
{
  "sub": "johndoe",
  "iat": 1710069600,
  "exp": 1710156000
}

Signature:
HMACSHA512(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)

Complete Token: header.payload.signature
```

## ✅ Security Checklist

Before deployment:
- [ ] Change `app.jwt.secret` to a random string
- [ ] Update database connection details
- [ ] Set `ddl-auto` to `validate` in production
- [ ] Enable HTTPS/TLS
- [ ] Update CORS origins to your frontend domain
- [ ] Implement rate limiting on `/api/auth/login`
- [ ] Setup error logging and monitoring
- [ ] Test with actual frontend application
- [ ] Review all credentials and secrets
- [ ] Create database backup before deploying

## 🚀 Ready to Use!

All security components are implemented and working together. You can now:
1. Register new users
2. Login with credentials
3. Use JWT tokens to access protected endpoints
4. Manage user authentication and authorization

**Next Step:** Create REST controllers for your business entities (Orders, Products, etc.)

