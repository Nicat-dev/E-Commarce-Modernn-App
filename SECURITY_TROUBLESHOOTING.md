# Spring Security Troubleshooting Guide

## Common Issues & Solutions

### 1. ❌ "User not found with username"
**Problem:** Login fails with "User not found" error
**Causes:**
- User hasn't registered yet
- Typo in username
- Username is case-sensitive

**Solution:**
```bash
# First register the user
POST /api/auth/register
{
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com",
  "username": "johndoe",  # Exactly as registered
  "password": "password123",
  "isActive": true
}

# Then login with exact username
POST /api/auth/login
{
  "username": "johndoe",  # Must match exactly
  "password": "password123"
}
```

---

### 2. ❌ "Cannot validate JWT token"
**Problem:** Invalid JWT token error when calling protected endpoints
**Causes:**
- Token expired (24 hours)
- Token corrupted
- Token signature tampered
- Wrong secret key

**Solution:**
```bash
# Get a new token
POST /api/auth/login
{
  "username": "johndoe",
  "password": "password123"
}

# Use the new token in requests
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <NEW_TOKEN>"

# Check token expiration time
# Default: 24 hours (86400000 milliseconds)
```

---

### 3. ❌ "Access is denied" / 403 Forbidden
**Problem:** Request returns 403 Forbidden error
**Causes:**
- Missing Authorization header
- Invalid token format
- Token expired
- User doesn't have required role

**Solution:**
```bash
# Make sure Authorization header is present and correctly formatted
# Format: Authorization: Bearer <token>

# Correct:
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."

# Incorrect:
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: eyJhbGciOiJIUzUxMiJ9..."  # Missing "Bearer"

curl -X GET http://localhost:8080/api/orders \
  -H "Token: eyJhbGciOiJIUzUxMiJ9..."  # Wrong header name
```

---

### 4. ❌ "Email already exists"
**Problem:** Registration fails with "Email already exists"
**Causes:**
- Email already registered
- Case sensitivity (emails are case-insensitive)

**Solution:**
```bash
# Use a different email address
POST /api/auth/register
{
  "name": "Jane Doe",
  "phone": "+0987654321",
  "email": "jane@example.com",  # Different email
  "username": "janedoe",
  "password": "password123",
  "isActive": true
}

# Or check existing users and delete if needed (admin only)
DELETE /api/users/{userId}
```

---

### 5. ❌ "Username already exists"
**Problem:** Registration fails with "Username already exists"
**Causes:**
- Username already registered
- Case sensitivity

**Solution:**
```bash
# Use a different username
POST /api/auth/register
{
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john2@example.com",
  "username": "johndoe2",  # Different username
  "password": "password123",
  "isActive": true
}
```

---

### 6. ❌ "Bad credentials"
**Problem:** Login fails with "Bad credentials"
**Causes:**
- Wrong password
- User doesn't exist
- User account is inactive

**Solution:**
```bash
# 1. Verify username exists
# 2. Check password is correct
# 3. Verify isActive = true
POST /api/auth/login
{
  "username": "johndoe",
  "password": "correctPassword123"  # Correct password
}

# If user is inactive, update user:
# PUT /api/users/{userId}
# {
#   "isActive": true
# }
```

---

### 7. ❌ CORS Error: "Access to XMLHttpRequest blocked"
**Problem:** Frontend cannot access API due to CORS error
**Causes:**
- Frontend origin not in CORS allowed list
- Browser sending OPTIONS preflight request

**Solution:**
Update `SecurityConfig.java` CORS configuration:
```java
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:3000",      // React
        "http://localhost:4200",      // Angular
        "http://localhost:8000",      // Your frontend URL
        "https://yourfrontend.com"    // Production frontend
    ));
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS"
    ));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

Then rebuild and restart:
```bash
./gradlew clean build
java -jar build/libs/E-Commarce-Modern-App-0.0.1-SNAPSHOT.jar
```

---

### 8. ❌ "User is not active"
**Problem:** Login fails with "User is not active"
**Causes:**
- User account is deactivated (isActive = false)
- Admin deactivated the account

**Solution:**
```bash
# Admin must update user to activate
PUT /api/users/{userId}
Authorization: Bearer <ADMIN_TOKEN>
{
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com",
  "username": "johndoe",
  "password": "password123",
  "isActive": true  # Set to true
}
```

---

### 9. ❌ Database Connection Error
**Problem:** "Cannot get a connection" or "Connection refused"
**Causes:**
- PostgreSQL not running
- Wrong database URL
- Wrong credentials
- Database doesn't exist

**Solution:**
```bash
# 1. Check PostgreSQL is running
# Windows:
pg_isready

# 2. Verify application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# 3. Create database if not exists
psql -U postgres
CREATE DATABASE ecommerce_db;
\q

# 4. Restart application
./gradlew bootRun
```

---

### 10. ❌ Port Already in Use (8080)
**Problem:** "Failed to bind to port 8080"
**Causes:**
- Another application using port 8080
- Previous instance still running

**Solution:**
```bash
# Change port in application.properties
server.port=8081

# Or kill the process using port 8080
# Windows (PowerShell):
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or restart application
./gradlew bootRun
```

---

### 11. ❌ "Invalid token" with Postman
**Problem:** Token works in curl but not in Postman
**Causes:**
- Wrong header format in Postman
- Token not being added properly

**Solution:**
In Postman:
1. Go to "Authorization" tab
2. Select "Bearer Token" from Type dropdown
3. Paste token in "Token" field
4. Do NOT add "Bearer " prefix (Postman adds it)

Or manually add header:
1. Go to "Headers" tab
2. Key: `Authorization`
3. Value: `Bearer eyJhbGciOiJIUzUxMiJ9...`

---

### 12. ❌ Token Not Generated After Login
**Problem:** Login response missing token
**Causes:**
- JwtProvider bean not initialized
- app.jwt.secret not configured
- Spring context issue

**Solution:**
```bash
# 1. Check logs for errors
tail -f application.log

# 2. Verify JwtProvider is a @Component
# src/main/java/com/project/ecommarcemodernapp/security/JwtProvider.java
@Component  # Make sure this annotation exists

# 3. Verify application.properties has JWT config
app.jwt.secret=mySecureSecretKeyForJWTTokenGenerationAndValidationPurposesOnly123456789
app.jwt.expiration=86400000

# 4. Restart application
./gradlew bootRun
```

---

### 13. ❌ "Cannot set user authentication"
**Problem:** CustomUserDetailsService throws exception
**Causes:**
- User not found in database
- User is inactive
- Database connection issue

**Solution:**
```bash
# 1. Check user exists in database
# Connect to PostgreSQL:
psql -U postgres -d ecommerce_db

# 2. Check users table:
SELECT * FROM users;

# 3. Verify user is active:
UPDATE users SET is_active = true WHERE username = 'johndoe';

# 4. Check database has correct data
SELECT id, username, email, password, is_active FROM users;
```

---

### 14. ❌ Gradle Build Failure - JWT Dependencies
**Problem:** Build fails with "Could not find io.jsonwebtoken:jjwt-api"
**Causes:**
- Gradle cache issue
- Repository not accessible
- Incomplete build.gradle

**Solution:**
```bash
# 1. Verify build.gradle has correct dependencies:
dependencies {
    // ... other dependencies ...
    implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'
    // ... other dependencies ...
}

# 2. Clean gradle cache
./gradlew clean

# 3. Rebuild
./gradlew build

# 4. If still fails, check internet connection
# JWT dependencies come from Maven Central Repository
```

---

### 15. ❌ "No suitable HttpMessageConverter found" for LoginResponse
**Problem:** Response serialization error
**Causes:**
- Missing Jackson dependency
- Record class not properly serialized

**Solution:**
```bash
# 1. Verify Jackson is in dependencies (auto-included with Spring Boot)
# 2. Check build.gradle includes:
implementation 'org.springframework.boot:spring-boot-starter-webmvc'

# 3. Verify response DTO is a record:
public record LoginResponse(
    String token,
    String type,
    Long id,
    String username,
    String email
) {}

# 4. Restart application
./gradlew bootRun
```

---

## 🔍 Debugging Tips

### Enable Debug Logging
```properties
# application.properties
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.security.crypto=DEBUG
logging.level.com.project.ecommarcemodernapp.security=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Check JWT Token Content
```javascript
// In browser console, decode JWT:
function decodeJWT(token) {
    const parts = token.split('.');
    const payload = JSON.parse(atob(parts[1]));
    console.log(payload);
}

decodeJWT("eyJhbGciOiJIUzUxMiJ9...");
// Output:
// {
//   "sub": "johndoe",
//   "iat": 1710069600,
//   "exp": 1710156000
// }
```

### Test Endpoints with curl
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","phone":"+1234567890","email":"john@example.com","username":"johndoe","password":"password123","isActive":true}' \
  -v

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"johndoe","password":"password123"}' \
  -v

# Save token to variable
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"johndoe","password":"password123"}' | jq -r '.token')

# Use token
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -v
```

### Check Database
```sql
-- Connect to PostgreSQL
psql -U postgres -d ecommerce_db

-- Check users
SELECT id, username, email, is_active, created_at FROM users;

-- Check orders
SELECT id, order_code, user_id, created_at FROM orders;

-- Check if user has orders
SELECT * FROM orders WHERE user_id = 1;
```

---

## 📞 Still Having Issues?

1. **Check Logs:** `tail -f logs/application.log`
2. **Verify Configuration:** Check `application.properties`
3. **Test Endpoint:** Use curl or Postman
4. **Check Database:** Verify data in PostgreSQL
5. **Restart Application:** `./gradlew bootRun`
6. **Check Gradle:** `./gradlew clean build`

---

## 🎯 Quick Checklist for New Developers

- [ ] PostgreSQL running
- [ ] Database `ecommerce_db` created
- [ ] JWT secret configured in `application.properties`
- [ ] CORS origins updated if needed
- [ ] All dependencies in `build.gradle`
- [ ] All security files created
- [ ] UserRepository has custom methods
- [ ] SecurityConfig has all beans
- [ ] AuthenticationController exists
- [ ] Can register user
- [ ] Can login and get token
- [ ] Can access protected endpoints with token

---

**Remember:** Always restart the application after configuration changes!

