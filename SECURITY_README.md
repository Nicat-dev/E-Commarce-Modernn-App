# Spring Security Configuration Documentation

## Overview
This document describes the complete Spring Security implementation for the E-Commerce Modern App using JWT (JSON Web Tokens) authentication.

## Components

### 1. **JwtProvider** (`security/JwtProvider.java`)
- Generates JWT tokens from authentication
- Validates JWT tokens
- Extracts username from JWT claims
- Uses HMAC-SHA512 signing algorithm
- Token expiration: 24 hours (configurable)

**Key Methods:**
- `generateToken(Authentication)` - Creates JWT from authenticated user
- `generateTokenFromUsername(String)` - Creates JWT from username
- `validateJwtToken(String)` - Validates token signature and expiration
- `getUserNameFromJwt(String)` - Extracts username from token

### 2. **CustomUserDetailsService** (`security/CustomUserDetailsService.java`)
- Implements Spring's `UserDetailsService` interface
- Loads user details from database by username
- Checks if user is active before returning user details
- Grants ROLE_USER by default

**Key Methods:**
- `loadUserByUsername(String)` - Loads user and checks if active
- `getAuthorities()` - Returns user roles

### 3. **JwtAuthenticationFilter** (`security/JwtAuthenticationFilter.java`)
- Extends `OncePerRequestFilter` to run once per request
- Extracts JWT token from Authorization header (Bearer token)
- Validates token using JwtProvider
- Sets Spring Security context if token is valid
- Allows request to continue even if token is invalid (public endpoints)

**How it works:**
1. Extract token from "Authorization: Bearer <token>" header
2. Validate token signature and expiration
3. Get username from token
4. Load UserDetails from database
5. Create UsernamePasswordAuthenticationToken
6. Set it in SecurityContext for request

### 4. **SecurityConfig** (`security/SecurityConfig.java`)
Complete Spring Security configuration with:

**Features:**
- ✅ JWT-based stateless authentication
- ✅ CORS enabled for frontend integration
- ✅ CSRF protection disabled (suitable for JWT APIs)
- ✅ Password encoder using BCrypt
- ✅ Custom authentication provider
- ✅ Public endpoints for login/register
- ✅ Protected endpoints requiring authentication

**Endpoint Authorization:**
```
POST /api/auth/login        - Public (anyone)
POST /api/auth/register     - Public (anyone)
GET  /api/products          - Public (anyone)
POST /api/products          - Admin only
PUT  /api/products/**       - Admin only
DELETE /api/products/**     - Admin only
All other endpoints         - Authenticated users only
```

**CORS Configuration:**
- Allowed origins: localhost:3000, localhost:4200
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS
- Allowed headers: All
- Credentials: Enabled
- Max age: 3600 seconds

### 5. **AuthenticationService** (`service/AuthenticationService.java`)
Service interface for authentication operations:
- `login(LoginRequest)` - Authenticate user and return JWT token
- `register(UserRequest)` - Register new user

### 6. **AuthenticationServiceImpl** (`service/impl/AuthenticationServiceImpl.java`)
Implementation of authentication service:
- Uses AuthenticationManager to authenticate credentials
- Generates JWT token on successful login
- Returns user details with token in LoginResponse
- Delegates user creation to UserService for registration

### 7. **AuthenticationController** (`controller/AuthenticationController.java`)
REST endpoints for authentication:
- `POST /api/auth/login` - Login endpoint
- `POST /api/auth/register` - Register endpoint
- CORS enabled for all origins
- Input validation using @Valid annotation

### 8. **DTOs**

**LoginRequest** - Input for login
```java
{
  "username": "string",
  "password": "string"
}
```

**LoginResponse** - Response after successful login
```java
{
  "token": "eyJhbGc...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}
```

**MessageResponse** - Generic response message
```java
{
  "message": "string",
  "success": true/false
}
```

## Security Flow

### Login Flow:
1. User sends credentials to `/api/auth/login`
2. AuthenticationManager verifies credentials
3. JwtProvider generates JWT token
4. Return token in LoginResponse
5. Client stores token in localStorage/sessionStorage

### Authenticated Request Flow:
1. Client sends request with "Authorization: Bearer <token>" header
2. JwtAuthenticationFilter intercepts request
3. Extracts and validates token
4. Loads user from database
5. Sets security context
6. Request proceeds if token is valid

### Logout Flow:
- Token is removed from client side (no server-side logout needed)
- Token expires automatically after 24 hours

## Configuration Properties

**application.properties:**
```properties
# JWT Configuration
app.jwt.secret=mySecureSecretKeyForJWTTokenGenerationAndValidationPurposesOnly123456789
app.jwt.expiration=86400000  # 24 hours in milliseconds

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Testing Authentication

### 1. Register a new user:
```bash
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com",
  "username": "johndoe",
  "password": "SecurePassword123",
  "isActive": true
}
```

### 2. Login to get token:
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePassword123"
}

# Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}
```

### 3. Use token in subsequent requests:
```bash
GET /api/orders
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Best Practices Implemented

✅ **Password Security** - Passwords are hashed using BCrypt before storage
✅ **Token Validation** - All tokens are validated for signature and expiration
✅ **Stateless Authentication** - No sessions stored on server (scalable)
✅ **User Status Check** - Inactive users cannot authenticate
✅ **Error Handling** - Invalid tokens are silently ignored for public endpoints
✅ **CORS Configuration** - Properly configured for frontend integration
✅ **Request Validation** - All inputs validated using @Valid annotation
✅ **Exception Handling** - Custom exceptions for authentication errors
✅ **Transaction Management** - @Transactional ensures data consistency

## Security Considerations

⚠️ **Important:** 
- Change `app.jwt.secret` to a long, random string in production
- Use HTTPS in production
- Store JWT tokens securely on client (avoid localStorage for sensitive data)
- Implement token refresh mechanism for better security
- Set appropriate CORS origins for your frontend URL

## Future Enhancements

- [ ] Implement refresh token mechanism
- [ ] Add role-based access control (RBAC)
- [ ] Implement email verification for registration
- [ ] Add password reset functionality
- [ ] Implement rate limiting for login attempts
- [ ] Add audit logging for security events

