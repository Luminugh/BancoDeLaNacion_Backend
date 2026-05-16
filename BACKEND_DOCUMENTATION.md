# Backend Technical Documentation

**Project:** Banco de la Nacion Backend

**Stack:** Java 17, Spring Boot 3.4.x, Spring Web, Spring Data JPA, Spring Security, Spring Validation, PostgreSQL, Lombok, springdoc-openapi

**Purpose:** This document describes the current backend architecture and API contract so another AI can generate a frontend in Next.js + React that integrates correctly with this backend.

---

## 1. Project Structure

The backend follows a layered architecture under the `com.bn` package.

### Main folders

- `config`
  - Application configuration classes.
  - Swagger/OpenAPI configuration.
  - Security configuration.
  - CORS configuration.
- `controllers`
  - REST API entry points.
  - Each controller exposes a resource-specific HTTP contract.
- `dto/request`
  - Incoming request payload contracts.
  - Validation annotations are applied here.
- `dto/response`
  - Outgoing response payload contracts.
  - Includes the global `ApiResponse<T>` wrapper.
- `entities`
  - JPA entities mapped to PostgreSQL tables.
- `enums`
  - Domain enumerations stored as strings in JPA entities.
- `exceptions`
  - Custom runtime exceptions and the global exception handler.
- `repositories`
  - Spring Data JPA repositories.
- `security`
  - JWT-related placeholders and authentication support classes.
- `services`
  - Service interfaces and implementations.
  - Service layer contains the application behavior exposed by controllers.
- `utils`
  - Shared constants.

### Request flow

The current flow is:

`Frontend -> Controller -> Service -> Repository -> PostgreSQL`

The controllers do not return entities directly. They return DTOs wrapped in `ApiResponse<T>`.

### Responsibility by layer

- `controllers`: HTTP routing, request validation trigger, status code selection.
- `services`: business-oriented orchestration, entity-to-DTO mapping, repository access.
- `repositories`: database persistence and retrieval.
- `entities`: ORM mapping to database tables.
- `dto`: contracts between frontend and backend.
- `exceptions`: standard error responses.
- `security`: JWT scaffolding and Spring Security setup.

---

## 2. Entities

All entities extend `BaseEntity`, which provides:

- `id : UUID`
- `createdAt : LocalDateTime`

`createdAt` is automatically set on persist using `@PrePersist`.

### BaseEntity

- **Table:** not mapped directly (`@MappedSuperclass`)
- **Fields:**
  - `id : UUID`
  - `createdAt : LocalDateTime`

---

### User

- **Table:** `profiles`
- **Purpose:** Represents a bank profile/customer/employee identity.

**Fields:**

- `dni : String`
- `firstName : String`
- `lastName : String`
- `email : String`
- `password : String`
- `phone : String`
- `role : UserRole`

**Relationships:**

- None directly in the current entity.
- Referenced by `Account`, `Loan`, `LoanApplication`, and `AuditLog`.

**Enums used:**

- `UserRole`

---

### Account

- **Table:** `accounts`
- **Purpose:** Represents a bank account linked to a profile.

**Fields:**

- `accountNumber : String`
- `balance : BigDecimal`
- `currency : String`
- `status : AccountStatus`
- `user : User`

**Relationships:**

- `ManyToOne -> User` via `user_id`

**Enums used:**

- `AccountStatus`

---

### Transaction

- **Table:** `transactions`
- **Purpose:** Stores a movement associated with an account.

**Fields:**

- `transactionType : TransactionType`
- `amount : BigDecimal`
- `description : String`
- `account : Account`

**Relationships:**

- `ManyToOne -> Account` via `account_id`

**Enums used:**

- `TransactionType`

---

### Transfer

- **Table:** `transfers`
- **Purpose:** Represents a transfer between two accounts.

**Fields:**

- `amount : BigDecimal`
- `status : TransferStatus`
- `senderAccount : Account`
- `receiverAccount : Account`

**Relationships:**

- `ManyToOne -> Account` via `sender_account_id`
- `ManyToOne -> Account` via `receiver_account_id`

**Enums used:**

- `TransferStatus`

---

### LoanApplication

- **Table:** `loan_applications`
- **Purpose:** Stores a credit application.

**Fields:**

- `requestedAmount : BigDecimal`
- `termMonths : Integer`
- `purpose : String`
- `monthlyIncome : BigDecimal`
- `status : ApplicationStatus`
- `user : User`

**Relationships:**

- `ManyToOne -> User` via `user_id`

**Enums used:**

- `ApplicationStatus`

---

### Loan

- **Table:** `loans`
- **Purpose:** Represents an active or historical loan.

**Fields:**

- `loanNumber : String`
- `amount : BigDecimal`
- `interestRate : BigDecimal`
- `termMonths : Integer`
- `monthlyPayment : BigDecimal`
- `remainingBalance : BigDecimal`
- `status : LoanStatus`
- `disbursementDate : LocalDate`
- `user : User`

**Relationships:**

- `ManyToOne -> User` via `user_id`

**Enums used:**

- `LoanStatus`

---

### LoanInstallment

- **Table:** `loan_installments`
- **Purpose:** Represents an installment linked to a loan.

**Fields:**

- `installmentNumber : Integer`
- `dueDate : LocalDate`
- `amount : BigDecimal`
- `paidAmount : BigDecimal`
- `paymentDate : LocalDateTime`
- `status : InstallmentStatus`
- `loan : Loan`

**Relationships:**

- `ManyToOne -> Loan` via `loan_id`

**Enums used:**

- `InstallmentStatus`

---

### Payment

- **Table:** `payments`
- **Purpose:** Represents a payment made against a loan.

**Fields:**

- `amount : BigDecimal`
- `paymentMethod : String`
- `loan : Loan`

**Relationships:**

- `ManyToOne -> Loan` via `loan_id`

**Enums used:**

- None

---

### AuditLog

- **Table:** `audit_logs`
- **Purpose:** Audit record for important actions.

**Fields:**

- `action : String`
- `tableName : String`
- `recordId : UUID`
- `user : User`

**Relationships:**

- `ManyToOne -> User` via `user_id`

**Enums used:**

- None

---

## 3. Enums

All enums are stored as strings in the database through JPA `@Enumerated(EnumType.STRING)`.

### UserRole

- `CUSTOMER`
- `EVALUATOR`
- `COMMITTEE`
- `ADMIN`

### AccountStatus

- `ACTIVE`
- `INACTIVE`
- `BLOCKED`

### TransactionType

- `DEPOSIT`
- `WITHDRAWAL`
- `TRANSFER_SENT`
- `TRANSFER_RECEIVED`
- `LOAN_PAYMENT`

### TransferStatus

- `PENDING`
- `COMPLETED`
- `FAILED`

### LoanStatus

- `ACTIVE`
- `COMPLETED`
- `LATE`

### InstallmentStatus

- `PENDING`
- `PAID`
- `OVERDUE`

### ApplicationStatus

- `SUBMITTED`
- `EVALUATION`
- `APPROVED`
- `REJECTED`
- `DISBURSED`

---

## 4. DTOs

The backend uses DTOs for API input and output. Entities are not exposed directly.

### Request DTOs

#### LoginRequestDTO

Used by `POST /api/auth/login`.

```json
{
  "email": "user@example.com",
  "password": "Secret123"
}
```

Fields:

- `email : String` with `@NotBlank`, `@Email`
- `password : String` with `@NotBlank`

#### AuthLoginRequestDTO

This file exists in the source tree but is currently unused by the active controller. It has the same structure as `LoginRequestDTO`.

#### RegisterRequestDTO

Used by `POST /api/auth/register` and `POST /api/profiles`.

```json
{
  "dni": "12345678",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@example.com",
  "password": "Secret123",
  "phone": "999888777",
  "role": "CUSTOMER"
}
```

Fields:

- `dni : String` with `@NotBlank`
- `firstName : String` with `@NotBlank`
- `lastName : String` with `@NotBlank`
- `email : String` with `@NotBlank`, `@Email`
- `password : String` with `@NotBlank`
- `phone : String` optional
- `role : UserRole` with `@NotNull`

#### AccountRequestDTO

Used by `POST /api/accounts`.

```json
{
  "accountNumber": "00123456789012345678",
  "currency": "PEN",
  "userId": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a"
}
```

Fields:

- `accountNumber : String` with `@NotBlank`
- `currency : String` with `@NotBlank`
- `userId : UUID` with `@NotNull`

#### TransferRequestDTO

Used by `POST /api/transfers`.

```json
{
  "senderAccountId": "d3f0d5f1-7d9a-4a1b-889d-7f4d1e8e4fca",
  "receiverAccountId": "a4b8c1b2-59df-49cf-86ad-1d2f84d61d56",
  "amount": 150.75,
  "description": "Transfer to savings"
}
```

Fields:

- `senderAccountId : UUID` with `@NotNull`
- `receiverAccountId : UUID` with `@NotNull`
- `amount : BigDecimal` with `@NotNull`, `@Positive`
- `description : String` optional

#### LoanRequestDTO

Used by `POST /api/loans`.

```json
{
  "requestedAmount": 10000,
  "termMonths": 24,
  "purpose": "Vehicle purchase",
  "monthlyIncome": 2500,
  "userId": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a"
}
```

Fields:

- `requestedAmount : BigDecimal` with `@NotNull`, `@Positive`
- `termMonths : Integer` with `@NotNull`
- `purpose : String` with `@NotBlank`
- `monthlyIncome : BigDecimal` with `@NotNull`, `@Positive`
- `userId : UUID` with `@NotNull`

#### LoanApplicationRequestDTO

Used by `POST /api/loan-applications`.

It has the same structure as `LoanRequestDTO` except it represents a request stage, not a loan disbursement.

```json
{
  "requestedAmount": 10000,
  "termMonths": 24,
  "purpose": "Home renovation",
  "monthlyIncome": 2500,
  "userId": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a"
}
```

---

### Response DTOs

#### ProfileResponseDTO

```json
{
  "id": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a",
  "dni": "12345678",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@example.com",
  "phone": "999888777",
  "role": "CUSTOMER",
  "createdAt": "2025-01-01T10:00:00"
}
```

#### AccountResponseDTO

```json
{
  "id": "9f26d8e1-7b48-4fc9-8fb4-61acb8a8f11d",
  "accountNumber": "00123456789012345678",
  "balance": 0.00,
  "currency": "PEN",
  "status": "ACTIVE",
  "userId": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a",
  "createdAt": "2025-01-01T10:00:00"
}
```

#### TransactionResponseDTO

```json
{
  "id": "ab2c3d4e-1234-5678-90ab-cdef12345678",
  "transactionType": "DEPOSIT",
  "amount": 200.00,
  "description": "Initial deposit",
  "accountId": "9f26d8e1-7b48-4fc9-8fb4-61acb8a8f11d",
  "createdAt": "2025-01-01T10:00:00"
}
```

#### TransferResponseDTO

```json
{
  "id": "be2c3d4e-1234-5678-90ab-cdef12345678",
  "amount": 150.75,
  "status": "PENDING",
  "senderAccountId": "d3f0d5f1-7d9a-4a1b-889d-7f4d1e8e4fca",
  "receiverAccountId": "a4b8c1b2-59df-49cf-86ad-1d2f84d61d56",
  "createdAt": "2025-01-01T10:00:00"
}
```

#### LoanResponseDTO

```json
{
  "id": "cc2c3d4e-1234-5678-90ab-cdef12345678",
  "loanNumber": "LN-1A2B3C4D",
  "amount": 10000.00,
  "interestRate": 0.00,
  "termMonths": 24,
  "monthlyPayment": 0.00,
  "remainingBalance": 10000.00,
  "status": "ACTIVE",
  "disbursementDate": null,
  "userId": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a",
  "createdAt": "2025-01-01T10:00:00"
}
```

#### LoanApplicationResponseDTO

```json
{
  "id": "dd2c3d4e-1234-5678-90ab-cdef12345678",
  "requestedAmount": 10000.00,
  "termMonths": 24,
  "purpose": "Home renovation",
  "monthlyIncome": 2500.00,
  "status": "SUBMITTED",
  "userId": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a",
  "createdAt": "2025-01-01T10:00:00"
}
```

#### AuthResponseDTO

```json
{
  "accessToken": "",
  "tokenType": "Bearer",
  "profile": {
    "id": "2c1a5a0a-5f8d-4db4-a7ed-7d3f8d0c8f4a",
    "dni": "12345678",
    "firstName": "Juan",
    "lastName": "Perez",
    "email": "juan@example.com",
    "phone": "999888777",
    "role": "CUSTOMER",
    "createdAt": "2025-01-01T10:00:00"
  }
}
```

---

## 5. API Response Standard

All exposed endpoints return data wrapped in `ApiResponse<T>`.

### Structure

- `success : boolean`
- `message : String`
- `data : T`
- `timestamp : LocalDateTime`

### Example JSON

```json
{
  "success": true,
  "message": "Accounts retrieved successfully",
  "data": [],
  "timestamp": "2025-01-01T10:00:00"
}
```

### Error example

```json
{
  "success": false,
  "message": "Profile not found",
  "data": null,
  "timestamp": "2025-01-01T10:00:00"
}
```

---

## 6. REST Endpoints

All endpoints currently return `ApiResponse<T>`.

### Auth

#### POST `/api/auth/register`

- **Description:** Registers a new user profile and returns token + profile payload.
- **Request body:** `RegisterRequestDTO`
- **Response body:** `ApiResponse<AuthResponseDTO>`
- **HTTP codes:**
  - `201 Created` on success
  - `400 Bad Request` on validation failure
  - `500 Internal Server Error` on unexpected errors

#### POST `/api/auth/login`

- **Description:** Authenticates by email and password, returns token + profile payload.
- **Request body:** `LoginRequestDTO`
- **Response body:** `ApiResponse<AuthResponseDTO>`
- **HTTP codes:**
  - `200 OK` on success
  - `400 Bad Request` on invalid credentials or validation failure
  - `404 Not Found` if profile does not exist
  - `500 Internal Server Error` on unexpected errors

---

### Profiles

#### GET `/api/profiles`

- **Description:** Lists all profiles.
- **Request body:** none
- **Response body:** `ApiResponse<List<ProfileResponseDTO>>`
- **HTTP codes:** `200 OK`

#### GET `/api/profiles/{id}`

- **Description:** Gets a profile by UUID.
- **Request body:** none
- **Response body:** `ApiResponse<ProfileResponseDTO>`
- **HTTP codes:**
  - `200 OK`
  - `404 Not Found`

#### POST `/api/profiles`

- **Description:** Creates a profile using the same structure as registration.
- **Request body:** `RegisterRequestDTO`
- **Response body:** `ApiResponse<ProfileResponseDTO>`
- **HTTP codes:**
  - `201 Created`
  - `400 Bad Request`

---

### Accounts

#### GET `/api/accounts`

- **Description:** Lists all accounts.
- **Response body:** `ApiResponse<List<AccountResponseDTO>>`
- **HTTP codes:** `200 OK`

#### GET `/api/accounts/{id}`

- **Description:** Gets an account by UUID.
- **Response body:** `ApiResponse<AccountResponseDTO>`
- **HTTP codes:**
  - `200 OK`
  - `404 Not Found`

#### POST `/api/accounts`

- **Description:** Creates an account linked to a profile.
- **Request body:** `AccountRequestDTO`
- **Response body:** `ApiResponse<AccountResponseDTO>`
- **HTTP codes:**
  - `201 Created`
  - `400 Bad Request`
  - `404 Not Found` if the profile does not exist

---

### Transactions

#### GET `/api/transactions`

- **Description:** Lists all transactions.
- **Response body:** `ApiResponse<List<TransactionResponseDTO>>`
- **HTTP codes:** `200 OK`

#### GET `/api/transactions/account/{accountId}`

- **Description:** Lists transactions filtered by account UUID.
- **Response body:** `ApiResponse<List<TransactionResponseDTO>>`
- **HTTP codes:** `200 OK`

---

### Transfers

#### POST `/api/transfers`

- **Description:** Creates a transfer request.
- **Request body:** `TransferRequestDTO`
- **Response body:** `ApiResponse<TransferResponseDTO>`
- **HTTP codes:**
  - `201 Created`
  - `400 Bad Request`

**Note:** The controller currently exposes only `POST`. The `TransferService` interface also contains `findAll()`, but no controller endpoint uses it.

---

### Loans

#### GET `/api/loans`

- **Description:** Lists all loans.
- **Response body:** `ApiResponse<List<LoanResponseDTO>>`
- **HTTP codes:** `200 OK`

#### GET `/api/loans/{id}`

- **Description:** Gets a loan by UUID.
- **Response body:** `ApiResponse<LoanResponseDTO>`
- **HTTP codes:**
  - `200 OK`
  - `404 Not Found`

#### POST `/api/loans`

- **Description:** Creates a loan record.
- **Request body:** `LoanRequestDTO`
- **Response body:** `ApiResponse<LoanResponseDTO>`
- **HTTP codes:**
  - `201 Created`
  - `400 Bad Request`
  - `404 Not Found` if the profile does not exist

---

### Loan Applications

#### GET `/api/loan-applications`

- **Description:** Lists loan applications.
- **Response body:** `ApiResponse<List<LoanApplicationResponseDTO>>`
- **HTTP codes:** `200 OK`

#### POST `/api/loan-applications`

- **Description:** Creates a loan application record.
- **Request body:** `LoanApplicationRequestDTO`
- **Response body:** `ApiResponse<LoanApplicationResponseDTO>`
- **HTTP codes:**
  - `201 Created`
  - `400 Bad Request`
  - `404 Not Found` if the profile does not exist

---

## 7. Security

### SecurityConfig

Current behavior:

- CSRF disabled.
- Session management is stateless.
- CORS is enabled.
- Swagger URLs are permitted.
- `/api/auth/**` is permitted.
- All other requests currently end with `permitAll()` as well.
- A `JwtFilter` is registered before `UsernamePasswordAuthenticationFilter`.
- `PasswordEncoder` bean uses `BCryptPasswordEncoder`.

### JwtFilter

- Extends `OncePerRequestFilter`.
- Current implementation is a pass-through filter.
- It does not parse tokens yet.
- It does not set authentication into the security context.

### JwtUtil

- Bean present and reads configuration values:
  - `bn.security.jwt.secret`
  - `bn.security.jwt.expiration-ms`
- Current methods are placeholders:
  - `generateToken(String username)` returns an empty string.
  - `extractUsername(String token)` returns `null`.
  - `isTokenValid(String token, String username)` returns `false`.

### CustomUserDetailsService

- Implements `UserDetailsService`.
- Currently throws `UsernameNotFoundException("Authentication not implemented yet")` for every lookup.

### What is already prepared

- Security package structure exists.
- Password encoding exists.
- JWT configuration keys exist.
- Filter and user details service placeholders exist.

### What is pending

- Real JWT token generation and parsing.
- Authentication context population.
- Authorization rules based on roles.
- Principal loading from database.
- Secured endpoint restrictions.

---

## 8. Validations

Spring Validation is used in request DTOs.

### Annotations currently used

- `@NotBlank`
- `@Email`
- `@NotNull`
- `@Positive`

### Where validation is applied

- `LoginRequestDTO`
- `RegisterRequestDTO`
- `AccountRequestDTO`
- `TransferRequestDTO`
- `LoanRequestDTO`
- `LoanApplicationRequestDTO`

### Validation handling

Validation errors are not handled in controllers.
They are intercepted globally by `GlobalExceptionHandler`.

---

## 9. Exception Handler

### GlobalExceptionHandler

Uses `@ControllerAdvice`.

### Handled exceptions

- `ResourceNotFoundException`
- `BadRequestException`
- `MethodArgumentNotValidException`
- `BindException`
- `ConstraintViolationException`
- generic `Exception`

### Error response format

Errors are returned using the same `ApiResponse<T>` wrapper with `success = false`.

Example:

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": ["must be a well-formed email address"]
  },
  "timestamp": "2025-01-01T10:00:00"
}
```

### Notes

- `ResourceNotFoundException` maps to `404 Not Found`.
- `BadRequestException` maps to `400 Bad Request`.
- Validation errors map to `400 Bad Request`.
- Unhandled exceptions map to `500 Internal Server Error`.

---

## 10. Swagger

Swagger/OpenAPI is configured using `springdoc-openapi-starter-webmvc-ui`.

### Available URLs

- `/swagger-ui.html`
- `/swagger-ui/index.html`
- `/v3/api-docs`

### Configuration class

- `SwaggerConfig`

### Documentation annotations

Controllers are annotated with:

- `@Tag`
- `@Operation`

This helps the generated OpenAPI spec describe the endpoints clearly for frontend integration.

---

## 11. CORS

CORS is configured in `WebConfig`.

### Current configuration

- Applies to `/api/**`
- Allowed origins are read from `bn.cors.allowed-origins`
- Default value: `http://localhost:3000`
- Allowed methods:
  - `GET`
  - `POST`
  - `PUT`
  - `PATCH`
  - `DELETE`
  - `OPTIONS`
- Allowed headers: `*`
- Credentials: enabled
- Max age: `3600`

### Purpose

This is prepared so a Next.js frontend running locally can call the backend without CORS issues.

---

## 12. Full Flow

### Normal request flow

1. Frontend sends an HTTP request to a controller endpoint.
2. Spring validates the request DTO if annotations are present.
3. The controller delegates to a service.
4. The service performs the current orchestration:
   - fetch entity through repository,
   - create entity,
   - map entity to response DTO.
5. The repository reads/writes PostgreSQL through JPA.
6. The service returns a response DTO.
7. The controller wraps that DTO in `ApiResponse<T>`.
8. The HTTP response is serialized to JSON.

### Error flow

1. Validation or business error happens.
2. Service throws a custom exception or Spring raises a validation exception.
3. `GlobalExceptionHandler` intercepts the error.
4. A consistent `ApiResponse` with `success = false` is returned.

### Data objects traveling across layers

- **Request:** DTO from frontend.
- **Domain:** JPA entity in service/repository layer.
- **Response:** DTO returned by service and wrapped in `ApiResponse<T>`.
- **Error:** Exception converted to `ApiResponse<T>` by the global handler.

---

## 13. Current Project State

### What already works

- Spring Boot application starts from `BankApplication`.
- Main layered structure is in place.
- Entities are mapped to PostgreSQL tables.
- Repositories extend `JpaRepository`.
- Controllers expose REST endpoints.
- DTO-based responses are used.
- `ApiResponse<T>` standard is in place.
- Swagger/OpenAPI is configured.
- CORS is configured for Next.js local development.
- Global exception handling is configured.

### What is prepared but not fully implemented

- JWT logic is only scaffolded.
- Role-based authorization is not enforced yet.
- `JwtUtil` methods are placeholders.
- `JwtFilter` does not authenticate requests yet.
- `CustomUserDetailsService` does not load users yet.
- Security currently permits all routes.

### What is intentionally simple right now

- No advanced banking workflows.
- No transaction posting logic.
- No transfer balance movement logic.
- No loan amortization logic.
- No payment registration logic.
- No audit logging workflow.

### Important integration note for frontend AI

The frontend should consume the backend through the documented endpoints and should expect responses in `ApiResponse<T>` format. The auth module already returns an `AuthResponseDTO` with a token placeholder and nested profile payload, which can be extended later without changing the response contract shape.

---

## 14. Practical Notes for Frontend Integration

- Use `http://localhost:8080` as the backend base URL during local development.
- Read the OpenAPI spec from `/v3/api-docs` to generate or verify frontend API clients.
- Use the `success`, `message`, and `data` fields from `ApiResponse<T>` rather than assuming raw arrays or objects.
- Expect validation errors to be returned in the same wrapper with `success = false`.
- Treat JWT as future-ready infrastructure: the current backend exposes the auth contract, but the token logic is still a placeholder.

---

## 15. Summary of Public API Surface

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`

### Profiles

- `GET /api/profiles`
- `GET /api/profiles/{id}`
- `POST /api/profiles`

### Accounts

- `GET /api/accounts`
- `GET /api/accounts/{id}`
- `POST /api/accounts`

### Transactions

- `GET /api/transactions`
- `GET /api/transactions/account/{accountId}`

### Transfers

- `POST /api/transfers`

### Loans

- `GET /api/loans`
- `GET /api/loans/{id}`
- `POST /api/loans`

### Loan applications

- `GET /api/loan-applications`
- `POST /api/loan-applications`

---

## 16. Final State for Another AI

This backend is currently a clean, documented API foundation for a bank-oriented frontend. Another AI can safely build a Next.js frontend by relying on:

- the endpoint list above,
- the DTO request/response contracts,
- the `ApiResponse<T>` wrapper,
- the Swagger spec,
- and the current CORS/security scaffolding.

The only major future work still pending is real JWT authentication and deeper banking workflows.
