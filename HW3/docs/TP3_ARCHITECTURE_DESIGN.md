# TP3 Architecture and Design Updates

## Architectural Overview

The TP3 phase extends the existing HW3 automated testing system with enhanced authentication, authorization, and validation capabilities. The architecture follows a layered approach with clear separation of concerns.

## System Architecture

### Layer Structure
```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│     (User Interface & Controllers)      │
├─────────────────────────────────────────┤
│            Service Layer                │
│  (Business Logic & Workflow Management) │
├─────────────────────────────────────────┤
│           Data Access Layer             │
│    (Repository & Entity Management)     │
├─────────────────────────────────────────┤
│           Infrastructure Layer          │
│   (Security, Validation, Utilities)    │
└─────────────────────────────────────────┘
```

## Key Architectural Components

### 1. Authentication & Authorization Module
**Location**: `src/main/java/edu/asu/cse360/hw3/security/`

**Components**:
- `UserAuthenticationService` - Core authentication logic
- `AuthenticationResult` - Result container with builder pattern
- `RoleBasedAccessController` - Permission management
- `AccessPermission` - Enumerated permission types

**Design Pattern**: Builder + Strategy Pattern
- Builder pattern for immutable result objects
- Strategy pattern for different authentication methods
- Enum-based permissions for type safety

### 2. Validation Pipeline Module
**Location**: `src/main/java/edu/asu/cse360/hw3/validation/`

**Components**:
- `PasswordStrengthValidator` - Password complexity validation
- `QuestionValidator` - Content and metadata validation
- `ValidationResult` - Standardized validation responses
- `ValidationChain` - Chain of responsibility implementation

**Design Pattern**: Chain of Responsibility + Command Pattern
- Validators linked in configurable chains
- Each validator implements common interface
- Validation results aggregated and returned

### 3. Database Integration Module
**Location**: `src/main/java/edu/asu/cse360/hw3/database/`

**Components**:
- `DatabaseCRUDService` - Generic CRUD operations
- `TransactionManager` - Transaction handling and rollback
- Entity classes with JPA annotations
- Repository interfaces following DAO pattern

**Design Pattern**: Repository + Unit of Work Pattern
- Repository abstraction for data access
- Unit of work for transaction boundaries
- Entity mappings using builder patterns

## UML Diagram Specifications

### Class Diagrams (Astah Required)

#### Authentication Classes
```
AuthenticationResult
├── Builder (static nested class)
├── success: boolean
├── username: String
├── userRole: String
├── sessionToken: String
├── errorMessage: String
└── requiresPasswordChange: boolean

UserAuthenticationService
├── authenticate(credentials): AuthenticationResult
├── validateSession(token): boolean
├── generateSessionToken(): String
└── requirePasswordChange(username): boolean
```

#### Validation Classes
```
PasswordStrengthValidator
├── PasswordStrength (enum)
├── validatePassword(password): ValidationResult
├── checkComplexity(password): StrengthLevel
└── generateSuggestions(password): List<String>

QuestionValidator
├── validateContent(question): ValidationResult
├── checkLength(content): boolean
├── validateMetadata(question): ValidationResult
└── sanitizeInput(content): String
```

### Sequence Diagrams (Astah Required)

#### Authentication Flow
1. User submits credentials
2. AuthenticationService validates credentials
3. Password strength validation (if required)
4. Session token generation
5. AuthenticationResult creation and return
6. Role-based permission assignment

#### Question Submission Flow
1. User submits question content
2. QuestionValidator performs content validation
3. Metadata validation and sanitization
4. Database persistence via CRUD service
5. Confirmation result returned to user

## Design Decision Rationale

### 1. Builder Pattern for Results
**Decision**: Use builder pattern for `AuthenticationResult` and `ValidationResult`
**Rationale**: 
- Immutability ensures thread safety
- Fluent API improves code readability
- Optional parameters handled elegantly
- Consistent with existing HW3 patterns

### 2. Enum-Based Permissions
**Decision**: Use `AccessPermission` enum instead of string-based permissions
**Rationale**:
- Compile-time type safety
- IDE auto-completion support
- Prevents typos in permission names
- Easy to extend with new permission types

### 3. Chain of Responsibility for Validation
**Decision**: Implement validation pipeline using chain pattern
**Rationale**:
- Validators can be combined and reordered
- New validation rules added without modifying existing code
- Each validator has single responsibility
- Easy to test individual validators in isolation

## Testing Architecture

### Test Structure Mapping
```
Production Code              Test Code
├── security/               ├── AuthenticationFlowTest
├── validation/             ├── ValidationPipelineTest
├── database/               ├── DatabaseIntegrationTest
└── service/                └── SystemIntegrationTest
```

### Test Categories
1. **Unit Tests**: Individual class behavior verification
2. **Integration Tests**: Multi-component workflow validation  
3. **System Tests**: End-to-end user scenario testing
4. **Performance Tests**: Response time and throughput validation

## Code Consistency Guidelines

### Naming Conventions
- Classes: PascalCase (e.g., `AuthenticationResult`)
- Methods: camelCase (e.g., `authenticateUser`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_PASSWORD_LENGTH`)
- Packages: lowercase with dots (e.g., `edu.asu.cse360.hw3.security`)

### Documentation Standards
- All public classes require comprehensive Javadoc
- Method documentation includes @param, @return, @throws
- Package-level documentation explains architectural decisions
- Code comments explain non-obvious business logic

### Error Handling
- Custom exceptions for domain-specific errors
- Consistent error message formatting
- Proper exception chaining for debugging
- Graceful degradation for non-critical failures

## Integration Points

### External Dependencies
- JUnit 5 for testing framework
- AssertJ for fluent assertions  
- H2 Database for integration testing
- Mockito for dependency mocking

### Configuration Management
- Properties files for environment-specific settings
- Builder patterns for configurable components
- Dependency injection for testability
- Environment variable overrides for deployment

---

**Last Updated**: November 9, 2025  
**Architecture Review**: Pending team approval  
**UML Diagrams Status**: To be created in Astah  
**Implementation Status**: In progress