# Security Implementation Architecture Diagrams

## 1️⃣ Complete Authentication Flow

```
USER WANTS TO LOGIN
        │
        ▼
┌──────────────────────────────────────┐
│ Frontend sends credentials            │
│ POST /api/auth/login                  │
│ Body: {username, password}            │
└────────────┬─────────────────────────┘
             │
             ▼
    ┌────────────────────┐
    │AuthenticationController
    └────────────┬───────┘
                 │
                 ▼
    ┌────────────────────────────────────┐
    │AuthenticationServiceImpl            │
    │ 1. Create auth token with creds    │
    └────────────┬─────────────────────┘
                 │
                 ▼
    ┌────────────────────────────────────┐
    │AuthenticationManager               │
    │ (from Spring Security)             │
    │ Authenticates user credentials     │
    └────────────┬─────────────────────┘
                 │
                 ▼
    ┌────────────────────────────────────┐
    │CustomUserDetailsService            │
    │ 1. Load user from DB by username   │
    │ 2. Check if user is active         │
    │ 3. Return UserDetails object       │
    └────────────┬─────────────────────┘
                 │
                 ▼
    ┌────────────────────────────────────┐
    │PasswordEncoder (BCrypt)            │
    │ Compares provided pwd with hashed  │
    │ password from database             │
    └────────────┬─────────────────────┘
                 │
          YES (Valid)
           /         \
          ▼           ▼ NO (Invalid)
        ✅         ❌
        
        ▼ (Valid)
    ┌────────────────────────────────────┐
    │JwtProvider                         │
    │ 1. Create JWT token                │
    │ 2. Add username to payload         │
    │ 3. Sign with HMAC-SHA512           │
    │ 4. Set expiration (24 hours)       │
    └────────────┬─────────────────────┘
                 │
                 ▼
    ┌────────────────────────────────────┐
    │Return LoginResponse                │
    │ {                                  │
    │   token: "eyJhbGc...",            │
    │   type: "Bearer",                  │
    │   id: 1,                           │
    │   username: "johndoe",             │
    │   email: "john@example.com"        │
    │ }                                  │
    └────────────┬─────────────────────┘
                 │
                 ▼
    ┌────────────────────────────────────┐
    │ Frontend receives token            │
    │ Stores in localStorage             │
    │ Ready to make authenticated requests
    └────────────────────────────────────┘
```

---

## 2️⃣ Authenticated Request Flow

```
FRONTEND MAKES AUTHENTICATED REQUEST
        │
        ├─ GET /api/orders
        ├─ Authorization: Bearer eyJhbGc...
        │
        ▼
┌──────────────────────────────────────┐
│ Request reaches Spring application   │
│ (Before reaching controller)          │
└────────────┬─────────────────────────┘
             │
             ▼
    ┌──────────────────────────────────────┐
    │ JwtAuthenticationFilter              │
    │ (OncePerRequestFilter)               │
    │                                      │
    │ 1. Extract Authorization header      │
    │ 2. Parse Bearer token                │
    │ 3. Validate token not null           │
    └────────────┬─────────────────────────┘
                 │
          Token exists?
           /         \
          ▼           ▼ No
        YES        Continue
        
        ▼
    ┌──────────────────────────────────────┐
    │ JwtProvider.validateJwtToken()       │
    │                                      │
    │ 1. Parse JWT (extract payload)       │
    │ 2. Verify signature                  │
    │ 3. Check expiration time             │
    └────────────┬─────────────────────────┘
                 │
        Valid token?
         /         \
        ▼           ▼ No
       YES      Reject
       
       ▼
    ┌──────────────────────────────────────┐
    │ Extract username from JWT payload    │
    │ username = token.getSubject()        │
    └────────────┬─────────────────────────┘
                 │
                 ▼
    ┌──────────────────────────────────────┐
    │ CustomUserDetailsService             │
    │ .loadUserByUsername(username)        │
    │                                      │
    │ 1. Query database for user           │
    │ 2. Check user is active              │
    │ 3. Return UserDetails object         │
    └────────────┬─────────────────────────┘
                 │
       User found & active?
         /         \
        ▼           ▼ No
       YES     Reject
       
       ▼
    ┌──────────────────────────────────────┐
    │ Create Authentication object         │
    │ UsernamePasswordAuthenticationToken  │
    │ - principal: userDetails             │
    │ - authorities: roles                 │
    │ - credentials: null (not needed)     │
    └────────────┬─────────────────────────┘
                 │
                 ▼
    ┌──────────────────────────────────────┐
    │ Set in SecurityContext               │
    │ SecurityContextHolder.getContext()   │
    │  .setAuthentication(auth)            │
    └────────────┬─────────────────────────┘
                 │
                 ▼
    ┌──────────────────────────────────────┐
    │ Continue filter chain                │
    │ Request reaches controller           │
    └────────────┬─────────────────────────┘
                 │
                 ▼
    ┌──────────────────────────────────────┐
    │ Controller Method Handler            │
    │                                      │
    │ Can access:                          │
    │ - SecurityContextHolder              │
    │ - Authentication object              │
    │ - UserDetails                        │
    │ - User roles/authorities             │
    └────────────┬─────────────────────────┘
                 │
                 ▼
    ┌──────────────────────────────────────┐
    │ Business Logic Execution             │
    │ (Service layer)                      │
    └────────────┬─────────────────────────┘
                 │
                 ▼
    ┌──────────────────────────────────────┐
    │ Return Response to Client            │
    │ (200 OK with data)                   │
    └────────────────────────────────────┘
```

---

## 3️⃣ Database Relationship Diagram

```
┌─────────────────────────┐
│       USERS             │
├─────────────────────────┤
│ id (PK)                 │
│ name                    │
│ phone                   │
│ email (UNIQUE)          │
│ username (UNIQUE)       │
│ password (hashed)       │
│ is_active               │
│ created_at              │
│ updated_at              │
└─────────────┬───────────┘
              │
              │ 1:N relationship
              │
              ▼
┌─────────────────────────┐
│       ORDERS            │
├─────────────────────────┤
│ id (PK)                 │
│ order_code (UNIQUE)     │
│ user_id (FK) ◄──────────┤
│ created_at              │
│ updated_at              │
└─────────────┬───────────┘
              │
              │ 1:N relationship
              │
              ▼
┌─────────────────────────┐
│    ORDER_ITEMS          │
├─────────────────────────┤
│ id (PK)                 │
│ order_id (FK) ◄─────────┤
│ product_id (FK)         │
│ quantity                │
│ created_at              │
│ updated_at              │
└─────────────┬───────────┘
              │
              │ N:1 relationship
              │
              ▼
┌─────────────────────────┐
│     PRODUCTS            │
├─────────────────────────┤
│ id (PK)                 │
│ name                    │
│ description             │
│ price                   │
│ created_at              │
│ updated_at              │
└─────────────────────────┘

Relationships:
- User has many Orders (1:N)
- Order has many OrderItems (1:N)
- OrderItem references Product (N:1)
- User -> Order -> OrderItem -> Product (chain)
```

---

## 4️⃣ Token Validation Decision Tree

```
Request arrives
        │
        ▼
Does request have Authorization header?
    ├─ NO  ──▶ Continue without authentication
    │         (public endpoint)
    │
    └─ YES ──▶ Is header format correct?
              │  (Bearer <token>)
              │
              ├─ NO  ──▶ Log error
              │         Continue without auth
              │
              └─ YES ──▶ Extract token
                        │
                        ▼
                Can JWT be parsed?
                    ├─ NO  ──▶ Invalid token error
                    │
                    └─ YES ──▶ Check signature
                              │  (HMAC-SHA512)
                              │
                              ├─ INVALID ──▶ Token tampered
                              │             error
                              │
                              └─ VALID ──▶ Check expiration
                                         │
                                         ├─ EXPIRED ──▶ Token expired
                                         │             error
                                         │
                                         └─ VALID ──▶ Extract username
                                                    │
                                                    ▼
                                         Load user from DB
                                                    │
                                         ┌──────────┴──────────┐
                                         │                     │
                                         ▼                     ▼
                                    User found?            Not found?
                                         │                     │
                                         ▼                     ▼
                                    Is active?            Reject request
                                         │
                                    ┌────┴────┐
                                    │          │
                                    ▼          ▼
                                  YES         NO
                                    │          │
                                    ▼          ▼
                           Set SecurityContext Reject
                           Allow request      request
```

---

## 5️⃣ Spring Security Component Interaction

```
┌────────────────────────────────────────────────────────────┐
│                                                            │
│  Spring Security Filter Chain (HttpSecurity)              │
│                                                            │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ 1. CORS Filter                                      │  │
│  │    (Handles cross-origin requests)                  │  │
│  └─────────────────────────────────────────────────────┘  │
│                    ↓                                        │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ 2. CSRF Filter (DISABLED for stateless API)         │  │
│  └─────────────────────────────────────────────────────┘  │
│                    ↓                                        │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ 3. JwtAuthenticationFilter                          │  │
│  │    └─ Validates JWT tokens                         │  │
│  │    └─ Sets SecurityContext                         │  │
│  └─────────────────────────────────────────────────────┘  │
│                    ↓                                        │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ 4. Authentication Filter                           │  │
│  │    (UsernamePasswordAuthenticationFilter)           │  │
│  │    └─ Only for /login endpoint                      │  │
│  └─────────────────────────────────────────────────────┘  │
│                    ↓                                        │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ 5. Authorization Filter                            │  │
│  │    └─ Checks permissions for endpoint               │  │
│  └─────────────────────────────────────────────────────┘  │
│                    ↓                                        │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ 6. Exception Translation Filter                     │  │
│  │    └─ Converts exceptions to HTTP responses         │  │
│  └─────────────────────────────────────────────────────┘  │
│                    ↓                                        │
│            CONTROLLER REACHED                              │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

---

## 6️⃣ SecurityConfig Bean Relationships

```
┌─────────────────────────┐
│   SecurityConfig        │
│   (@Configuration)      │
└────────────┬────────────┘
             │
        Creates:
         │
    ┌────┼────┬────────────┬────────────┐
    │    │    │            │            │
    ▼    ▼    ▼            ▼            ▼
   ┌───────────────┐  ┌──────────────────────────┐
   │PasswordEncoder│  │ DaoAuthenticationProvider│
   │ (BCrypt)      │  │                          │
   └───────────────┘  │ Uses:                    │
                      │ - UserDetailsService     │
                      │ - PasswordEncoder        │
                      └──────────────────────────┘
                      
                      ▼
                  
    ┌──────────────────────┐
    │ AuthenticationManager │
    │                      │
    │ Uses:                │
    │ - DaoAuthProvider    │
    └──────────────────────┘
    
    ┌──────────────────────┐
    │ SecurityFilterChain  │
    │                      │
    │ Configures:          │
    │ - CORS               │
    │ - CSRF disable       │
    │ - Session: STATELESS │
    │ - Authorization      │
    │ - Filter order       │
    └──────────────────────┘
    
    ┌──────────────────────┐
    │ JwtAuthenticationFilter
    │                      │
    │ Dependencies:        │
    │ - JwtProvider        │
    │ - UserDetailsService │
    └──────────────────────┘
    
    ┌──────────────────────┐
    │ CorsConfigurationSource
    │                      │
    │ Configures:          │
    │ - Origins            │
    │ - Methods            │
    │ - Headers            │
    │ - Credentials        │
    └──────────────────────┘
```

---

## 7️⃣ User Registration & Login Flow Comparison

```
REGISTRATION FLOW              LOGIN FLOW
═══════════════════            ══════════════

POST /api/auth/register        POST /api/auth/login
│                              │
▼                              ▼
Validate input                 Validate input
│                              │
▼                              ▼
Check email unique             Authenticate credentials
│                              │
▼                              ▼
Check username unique          Verify username & password
│                              │
▼                              ▼
Hash password (BCrypt)         Load UserDetails
│                              │
▼                              ▼
Save user to DB                Generate JWT token
│                              │
▼                              ▼
Return success message         Return token + user info
                               │
                               ▼
                               Client stores token
                               │
                               ▼
                               Use in Authorization header
```

---

## 8️⃣ Error Handling Flow

```
Exception occurs
        │
        ▼
Is it a validation error?
    ├─ YES ──▶ Return 400 Bad Request
    │         with validation messages
    │
    └─ NO  ──▶ Is it an authentication error?
              │
              ├─ YES ──▶ Return 401 Unauthorized
              │
              └─ NO  ──▶ Is it an authorization error?
                        │
                        ├─ YES ──▶ Return 403 Forbidden
                        │
                        └─ NO  ──▶ Is it a business error?
                                  │
                                  ├─ YES ──▶ Return 400/404/500
                                  │
                                  └─ NO  ──▶ Return 500 Internal Error
```

---

## 9️⃣ Complete Request-Response Lifecycle

```
CLIENT SIDE                    SERVER SIDE
═════════════════              ═════════════════════════════════════

1. User registers
   POST /api/auth/register ────────────────▶ AuthenticationController
   {                                        │
     name, phone, email,                    ▼
     username, password, isActive       AuthenticationServiceImpl
   }                                        │
                                          ▼
                                      UserServiceImpl
                                          │
                                          ▼
                                      Validation
                                      - Email unique
                                      - Username unique
                                          │
                                          ▼
                                      Hash password (BCrypt)
                                          │
                                          ▼
                                      UserRepository.save()
                                          │
                                          ▼
   ◄────────────────────────── MessageResponse
   {                             {
     message: "...",              message: "User registered",
     success: true                success: true
   }                            }

2. User logs in
   POST /api/auth/login ──────────────────▶ AuthenticationController
   {                                        │
     username, password                     ▼
   }                                    AuthenticationServiceImpl
                                            │
                                            ▼
                                        AuthenticationManager
                                            │
                                            ▼
                                        PasswordEncoder
                                        (verify password)
                                            │
                                            ▼
                                        JwtProvider
                                        (generate token)
                                            │
                                            ▼
   ◄────────────────────────── LoginResponse
   {                            {
     token: "eyJhbGc...",        token: "...",
     type: "Bearer",             type: "Bearer",
     id, username, email         id, username, email
   }                            }

3. Client makes authenticated request
   Store token in localStorage

4. Authenticated API call
   GET /api/orders ───────────────────────▶ JwtAuthenticationFilter
   Authorization: Bearer <token>           │
                                          ▼
                                      Extract & validate token
                                          │
                                          ▼
                                      Load UserDetails
                                          │
                                          ▼
                                      Set SecurityContext
                                          │
                                          ▼
                                      OrderController
                                          │
                                          ▼
                                      OrderService
                                          │
                                          ▼
                                      OrderRepository
                                          │
                                          ▼
   ◄────────────────────────── Response
   {                             [
     List of orders                {id, orderCode, userId, ...},
   }                               {id, orderCode, userId, ...}
                                ]
```

---

## 🔟 Token Lifecycle

```
TOKEN CREATED                  TOKEN IN USE                   TOKEN EXPIRED
═══════════════════            ════════════════               ═════════════════

User logs in
│
▼
Generate JWT with:
├─ subject (username)
├─ issued at (now)
├─ expiration (now + 24h)
└─ signature (HMAC-SHA512)

│
▼
Token = header.payload.signature

│
▼
Return to client
│
▼
Client stores token
│
└──────────────────────┐
                       │
                       ▼
              Client sends in header:
              Authorization: Bearer <token>
              
              │
              ▼
              Server receives request
              │
              ▼
              JwtAuthenticationFilter
              extracts token
              │
              ▼
              JwtProvider validates:
              ├─ Check signature
              ├─ Check expiration
              └─ Extract payload
              
              │
              ▼
              Token is VALID
              │
              ├─ YES ──▶ Process request
              │         │
              │         ▼
              │    Return response
              │
              └─ NO ──▶ 24 hours passed
                       │
                       ▼
                   Token EXPIRED
                   │
                   ▼
                   Return 401 Unauthorized
                   │
                   ▼
                   Client must login again
                   │
                   ▼
                   Get new token
                   │
                   ▼
                   Cycle repeats
```

---

**These diagrams illustrate the complete Spring Security implementation and how all components work together.**

