# E-Commerce Modern App - Best Practices Checklist & Troubleshooting

## ✅ Best Practices Implemented

### Architecture & Design Patterns
- [x] **Layered Architecture**: Controller → Service → Repository → Database
- [x] **Dependency Injection**: All dependencies injected via constructor
- [x] **Repository Pattern**: Spring Data JPA repositories for data access
- [x] **Service Pattern**: Business logic separated from controllers
- [x] **DTO Pattern**: Request/Response DTOs separate from entities
- [x] **Mapper Pattern**: MapStruct for entity-to-DTO conversion
- [x] **Builder Pattern**: Lombok @Builder for entity construction
- [x] **Strategy Pattern**: Predicates for validation logic
- [x] **Exception Handling Pattern**: Custom exceptions with enum status codes

### Code Quality
- [x] **Immutability**: Record types for DTOs
- [x] **Null Safety**: Optional for handling null values
- [x] **DRY Principle**: Reusable mappers, validators, and services
- [x] **SOLID Principles**: 
  - Single Responsibility: Each class has one reason to change
  - Open/Closed: Open for extension via interfaces
  - Liskov Substitution: Interface contracts followed
  - Interface Segregation: Focused interfaces
  - Dependency Inversion: Depends on abstractions

### Database Design
- [x] **Normalization**: Proper entity relationships
- [x] **Constraints**: NOT NULL, UNIQUE, FOREIGN KEY constraints
- [x] **Indexes**: Primary keys and unique columns indexed
- [x] **LAZY Loading**: Relationships loaded on demand
- [x] **Cascade Operations**: DELETE cascades to child entities
- [x] **Timestamp Tracking**: CreatedAt and UpdatedAt on all entities
- [x] **Connection Pooling**: HikariCP configuration

### Security
- [x] **Password Hashing**: BCrypt encryption for passwords
- [x] **JWT Authentication**: Stateless token-based auth
- [x] **Role-Based Access**: @PreAuthorize for method security
- [x] **CSRF Protection**: Disabled for stateless REST APIs
- [x] **Input Validation**: All inputs validated before processing
- [x] **Error Messages**: No sensitive data in error responses
- [x] **CORS**: Properly configured for frontend origin
- [x] **HTTP Security Headers**: Configured in SecurityConfig

### API Design
- [x] **RESTful Conventions**: Proper HTTP methods (GET, POST, PUT, DELETE)
- [x] **Status Codes**: Correct HTTP status codes for responses
- [x] **Resource Naming**: Plural nouns for endpoints (/users, /products)
- [x] **Versioning Ready**: Can extend to /api/v1/
- [x] **Response Format**: Consistent JSON response structure
- [x] **Error Responses**: Standardized error format
- [x] **HATEOAS Ready**: Can add links in responses

### Validation
- [x] **Input Validation**: Jakarta validation annotations
- [x] **Message Externalization**: messages.properties for i18n
- [x] **Cascading Validation**: @Valid on nested objects
- [x] **Custom Validators**: Predicates for business rules
- [x] **Business Logic Validation**: Service-level checks

### Performance
- [x] **Query Optimization**: LAZY loading, batch processing
- [x] **Connection Pooling**: HikariCP with proper sizing
- [x] **Compile-Time Processing**: MapStruct, Lombok
- [x] **Transaction Boundaries**: Proper @Transactional usage
- [x] **Database Indexing**: Natural keys indexed

### Observability & Logging
- [x] **SLF4J Logging**: Structured logging throughout
- [x] **Log Levels**: DEBUG, INFO, WARN, ERROR appropriately used
- [x] **Request Logging**: Controllers log operations
- [x] **Error Logging**: Exceptions logged with stack traces
- [x] **Security Logging**: Authentication attempts logged
- [x] **No Sensitive Data**: Passwords and tokens never logged

### Testing & Maintainability
- [x] **Comments**: Javadoc on public methods
- [x] **Class Documentation**: Class-level JavaDoc
- [x] **Clear Naming**: Self-explanatory method and variable names
- [x] **Constants**: Magic strings extracted to constants
- [x] **Configuration Externalization**: Properties externalized
- [x] **Consistent Formatting**: Code style guidelines followed

---

## 🔧 Dependency Management

### Core Dependencies
```gradle
// Spring Boot Starters
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-validation'

// Database
runtimeOnly 'org.postgresql:postgresql'

// JWT
implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'

// JSON
implementation 'com.fasterxml.jackson.core:jackson-databind:2.21.1'

// Mapping
implementation 'org.mapstruct:mapstruct:1.5.5.Final'
annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'

// Lombok
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok-mapstruct-binding:0.2.0'

// Testing
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation 'org.springframework.security:spring-security-test'
```

### Dependency Version Notes
- **Spring Boot 4.0.3**: Latest version with Jakarta EE namespaces
- **Java 25**: Latest LTS-adjacent version
- **jjwt 0.12.3**: Latest JWT library with modern API
- **MapStruct 1.5.5.Final**: Stable version with compile-time mapping
- **Lombok 1.18.30+**: Latest with MapStruct binding support
- **PostgreSQL JDBC 42.7.3+**: Latest driver version

---

## 🚨 Troubleshooting Guide

### Issue: JPA Entity Manager Factory Not Found
**Symptom**: `Cannot resolve reference to bean 'jpaSharedEM_entityManagerFactory'`

**Solution**: Add to main application class:
```java
@EnableJpaRepositories(basePackages = "com.project.ecommarcemodernapp.repository")
@EntityScan(basePackages = "com.project.ecommarcemodernapp.model")
```

**Cause**: Spring couldn't find entity or repository classes

---

### Issue: ObjectMapper Not Autowired
**Symptom**: `No bean named 'objectMapper'`

**Solution**: Add to SecurityConfig:
```java
@Bean
public ObjectMapper objectMapper() {
    return new ObjectMapper();
}
```

**Cause**: ObjectMapper bean wasn't defined as a Spring bean

---

### Issue: User.builder() Method Not Found
**Symptom**: `No candidates found for method call User.builder()`

**Solution**: 
1. Add @Builder to entity class
2. Rebuild project with `./gradlew clean build`
3. Rebuild IDE indices (Ctrl+Shift+F5)

**Cause**: Lombok @Builder not processed during compilation

---

### Issue: MapStruct Mapper Not Generated
**Symptom**: Mapper injection fails at runtime

**Solution**:
1. Rebuild project: `./gradlew clean build`
2. Clear IDE cache and rebuild
3. Ensure `@Mapper(componentModel = "spring")`
4. Check annotation processor configuration in gradle

**Cause**: MapStruct processor didn't generate implementation class

---

### Issue: JWT Token Validation Always Fails
**Symptom**: 401 Unauthorized on all authenticated endpoints

**Solution**:
1. Verify JWT secret is BASE64 encoded
2. Check token not expired (24 hours)
3. Verify Authorization header format: `Bearer <token>`
4. Check JwtProvider is properly initialized

**Cause**: Token configuration or header format issue

---

### Issue: CORS Error in Browser
**Symptom**: `Access-Control-Allow-Origin` error

**Solution**: Update SecurityConfig CORS configuration:
```java
configuration.setAllowedOrigins(List.of("http://localhost:3000"));
```

**Cause**: Frontend origin not allowed in CORS config

---

### Issue: Database Connection Refused
**Symptom**: `Connection refused` on startup

**Solution**:
1. Ensure PostgreSQL is running: `psql --version`
2. Check credentials in application.properties
3. Create database: `CREATE DATABASE ecommerce_db;`
4. Test connection: `psql -h localhost -U postgres -d ecommerce_db`

**Cause**: Database not running or incorrect credentials

---

### Issue: Hibernate DDL Auto Fails
**Symptom**: SQL syntax errors on application startup

**Solution**:
1. Set `spring.jpa.hibernate.ddl-auto=validate` after first run
2. Clear database and restart with `create`
3. Check entity annotations for typos
4. Verify PostgreSQL dialect is correct

**Cause**: Entity mapping issues or DDL syntax errors

---

### Issue: Password Never Matches After Hashing
**Symptom**: Login always fails with valid credentials

**Solution**:
1. Verify password hashed with BCrypt: `new BCryptPasswordEncoder().encode()`
2. Check CustomUserDetailsService loads active users only
3. Verify AuthenticationManager is properly configured
4. Test locally first: `passwordEncoder.matches(rawPassword, hashedPassword)`

**Cause**: Password not hashed before storage or service not using encoder

---

### Issue: LazyInitializationException on Endpoint
**Symptom**: `failed to lazily initialize a collection`

**Solution**:
1. Load data within @Transactional boundary
2. Use `fetch = FetchType.EAGER` if needed (performance tradeoff)
3. Use `@Transactional(readOnly = true)` for queries
4. Use DTO projection to fetch only needed fields

**Cause**: Lazy-loaded collection accessed outside transaction

---

### Issue: Mapper Not Converting Nested Objects
**Symptom**: Nested DTOs remain null

**Solution**:
1. Add mapper to `uses` parameter: `@Mapper(componentModel = "spring", uses = NestedMapper.class)`
2. Verify nested mapper has toDto/toEntity methods
3. Check field names match or use @Mapping annotations

**Cause**: MapStruct doesn't know how to map nested objects

---

### Issue: Custom Validation Message Not Appearing
**Symptom**: Default message instead of custom message

**Solution**:
1. Verify messages.properties has key
2. Restart application to reload properties
3. Check message key format: `{key.name}`
4. Use exact case matching

**Cause**: Message file not found or property key mismatch

---

### Issue: Role-Based Security Not Working
**Symptom**: @PreAuthorize('ROLE_ADMIN') always fails

**Solution**:
1. Check CustomUserDetailsService.getAuthorities() returns correct roles
2. Verify authority name format: "ROLE_USER", "ROLE_ADMIN"
3. Check @EnableMethodSecurity(prePostEnabled = true) on SecurityConfig
4. Enable debug logging: `logging.level.org.springframework.security=DEBUG`

**Cause**: Authority names don't include ROLE_ prefix or endpoint not protected

---

### Issue: Validation Errors Not Caught
**Symptom**: Invalid data stored despite @Valid annotations

**Solution**:
1. Add `@Valid` on request body parameter
2. Ensure controller method has validation exception handler
3. Check GlobalExceptionHandler is on classpath
4. Verify Bean Validation library is included

**Cause**: @Valid annotation missing or exception handler not registered

---

## 📊 Performance Tuning Recommendations

### Database Queries
```properties
# Enable query statistics
spring.jpa.properties.hibernate.generate_statistics=true

# Set batch size
spring.jpa.properties.hibernate.jdbc.batch_size=20

# Enable second-level cache
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
```

### Connection Pool
```properties
# Optimize pool size
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=20000
```

### Lazy Loading Strategy
- Use LAZY for One-to-Many relationships
- Use EAGER sparingly for One-to-One relationships
- Load within @Transactional boundaries

---

## 🔐 Security Hardening Checklist

### Before Production
- [ ] Enable HTTPS/SSL
- [ ] Use strong JWT secret (min 256 bits)
- [ ] Set JWT expiration appropriate for use case
- [ ] Implement refresh token mechanism
- [ ] Add rate limiting to prevent brute force
- [ ] Implement OWASP security headers
- [ ] Add request/response logging
- [ ] Use environment variables for secrets
- [ ] Implement API versioning
- [ ] Add request signature verification
- [ ] Implement monitoring and alerting
- [ ] Conduct security audit
- [ ] Enable SQL injection prevention (parameterized queries)
- [ ] Add OWASP Top 10 protections

---

## 📝 Git Best Practices

### .gitignore Template
```
# IDE
.idea/
.vscode/
*.swp
*.iml

# Build
build/
.gradle/
gradle-wrapper.jar

# Environment
.env
.env.local
application-local.properties

# OS
.DS_Store
Thumbs.db

# Logs
logs/
*.log
```

---

## 🚀 Deployment Checklist

### Pre-Deployment
- [ ] All tests passing
- [ ] Code review completed
- [ ] Sensitive data externalized to environment variables
- [ ] Database migrations tested
- [ ] Performance testing completed
- [ ] Security scanning passed
- [ ] Load balancer configured
- [ ] Logging aggregation setup

### Deployment
- [ ] Database backup before migration
- [ ] Zero-downtime deployment strategy
- [ ] Health check endpoints working
- [ ] Monitoring and alerting in place
- [ ] Rollback plan documented
- [ ] Documentation updated

---

## 📚 Reference Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [MapStruct Documentation](https://mapstruct.org/)
- [JWT.io](https://jwt.io/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [RESTful API Best Practices](https://restfulapi.net/)
- [OWASP Security Guidelines](https://owasp.org/)

---

**Last Updated**: May 4, 2026
**Version**: 1.0.0
**Maintainer**: Development Team

