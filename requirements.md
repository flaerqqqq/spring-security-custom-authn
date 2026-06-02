# Spring Security Practice Task: Dual Authentication Without JWT

## Goal

Build a Spring Boot REST application that helps you practice the full Spring Security authentication mechanism without JWT.

The main goal is to understand and implement the flow from an unauthenticated request token to an authenticated `Authentication` stored in the `SecurityContext`.

You should practice:

- `Authentication` as a request object and as an authenticated result
- `AuthenticationManager`
- `ProviderManager`
- `AuthenticationProvider`
- `DaoAuthenticationProvider`
- `UserDetailsService`
- `UserDetails`
- `PasswordEncoder`
- custom authentication token
- custom authentication provider
- `SecurityContext`
- `SecurityContextHolder`
- `SecurityContextRepository`
- session-based authentication
- logout
- brute-force protection
- authorization rules

This task intentionally does **not** use JWT. JWT parsing, signing, and validation are not part of this assignment.

---

## Project Context

You are building a generic Spring Boot REST application with user accounts, authentication endpoints, protected resource endpoints, and admin endpoints.

Users can authenticate in two ways:

1. Username + password login
2. One-time access code login

After successful authentication, the authenticated user should be stored in the `SecurityContext` and persisted through the HTTP session.

---

## Authentication Methods

### 1. Password Login

Endpoint:

```http
POST /api/v1/auth/login/password
```

Request body:

```json
{
  "username": "user_001",
  "password": "password123"
}
```

Expected behavior:

- The controller receives username and password.
- The service creates an unauthenticated username/password authentication token.
- The token is passed to `AuthenticationManager.authenticate(...)`.
- The authentication manager delegates to the configured provider chain.
- Username/password authentication is handled by `DaoAuthenticationProvider`.
- The provider uses `UserDetailsService` to load the user.
- The provider uses `PasswordEncoder` to compare raw and encoded passwords.
- On success, an authenticated `Authentication` is returned.
- The authentication result is stored in a new `SecurityContext`.
- The security context is saved using `SecurityContextRepository`.

---

### 2. One-Time Code Login

Endpoint:

```http
POST /api/v1/auth/login/code
```

Request body:

```json
{
  "username": "user_001",
  "code": "482911"
}
```

Expected behavior:

- The controller receives username and one-time code.
- The service creates a custom unauthenticated one-time-code authentication token.
- The token is passed to `AuthenticationManager.authenticate(...)`.
- The authentication manager delegates to a custom one-time-code authentication provider.
- The custom provider loads the user through `UserDetailsService`.
- The custom provider checks whether the user is active and not locked.
- The custom provider validates the one-time code.
- The code can be used only once.
- On success, the provider returns an authenticated custom authentication token.
- The authentication result is stored in a new `SecurityContext`.
- The security context is saved using `SecurityContextRepository`.

---

## Public Endpoints

These endpoints should be accessible without authentication:

```text
POST /api/v1/users/register
POST /api/v1/auth/login/password
POST /api/v1/auth/login/code
POST /api/v1/auth/code/generate
GET  /api/v1/public/ping
```

---

## Authenticated Endpoints

These endpoints require an authenticated user:

```text
GET  /api/v1/users/me
POST /api/v1/auth/logout
GET  /api/v1/resources
POST /api/v1/resources
```

---

## Admin-Only Endpoint

This endpoint requires an authenticated user with admin authority:

```text
GET /api/v1/admin/users
```

---

## User Model Requirements

Create a user model with at least the following fields:

```text
id
publicId
username
password
role
status
createdAt
updatedAt
```

### Field Meaning

| Field | Meaning |
|---|---|
| `id` | Internal database identifier |
| `publicId` | External stable identifier safe for APIs |
| `username` | Login identifier |
| `password` | Encoded password, never raw password |
| `role` | User role, for example `USER` or `ADMIN` |
| `status` | Account status, for example `ACTIVE`, `DISABLED`, `LOCKED` |
| `createdAt` | Creation timestamp |
| `updatedAt` | Last update timestamp |

### Role Requirements

Support at least:

```text
USER
ADMIN
```

### Status Requirements

Support at least:

```text
ACTIVE
DISABLED
LOCKED
```

---

## Registration Requirements

Endpoint:

```http
POST /api/v1/users/register
```

Registration should:

- Validate that username is unique.
- Generate a public user id.
- Encode the raw password before saving.
- Assign a default role.
- Assign an active status.
- Save the user in the database.
- Never return the password in the response.

Registration should **not** authenticate the user automatically unless you intentionally decide to add that as a separate optional feature.

---

## Password Encoding Requirements

- Raw passwords must never be stored.
- Passwords must be encoded during registration.
- Authentication must compare the raw login password against the encoded password.
- Use a real password encoder, not plain text.

Recommended encoder:

```text
BCryptPasswordEncoder
```

---

## UserDetailsService Requirements

Create a custom `UserDetailsService` that loads users from the database.

It should:

- Load user by username.
- Throw the correct authentication exception when user is not found.
- Convert your domain user into a Spring Security user representation.

Important distinction:

```text
Domain User = your database entity
UserDetails = Spring Security view of the user
```

---

## UserDetails Requirements

Create a custom `UserDetails` implementation or adapt your user model to Spring Security.

It should expose:

- username
- encoded password
- authorities
- enabled/disabled state
- locked/unlocked state

Rules:

- `ACTIVE` users can authenticate.
- `DISABLED` users cannot authenticate.
- `LOCKED` users cannot authenticate.
- `USER` role should map to `ROLE_USER`.
- `ADMIN` role should map to `ROLE_ADMIN`.

---

## AuthenticationManager Requirements

Use Spring Security's `AuthenticationManager`.

You should not manually verify passwords in your authentication service.

Password verification should happen through:

```text
AuthenticationManager
  -> ProviderManager
  -> DaoAuthenticationProvider
  -> UserDetailsService
  -> PasswordEncoder
```

Your authentication service should call:

```text
authenticationManager.authenticate(...)
```

The returned authentication result should be used as the trusted authenticated user.

---

## Custom One-Time-Code Authentication Requirements

Create a custom authentication mechanism for one-time code login.

You need the following pieces:

```text
One-time-code authentication token
One-time-code authentication provider
One-time-code service
```

### Custom Authentication Token Requirements

The custom token should represent two states:

1. Unauthenticated request
2. Authenticated result

Unauthenticated state should contain:

```text
principal = username
credentials = one-time code
authenticated = false
```

Authenticated state should contain:

```text
principal = authenticated user details
credentials = null or erased
authorities = user authorities
authenticated = true
```

### Custom Authentication Provider Requirements

The custom provider should:

- Support only the custom one-time-code authentication token.
- Extract username from the principal.
- Extract code from credentials.
- Load user using `UserDetailsService`.
- Check whether the user can authenticate.
- Validate the one-time code.
- Delete the one-time code after successful login.
- Return an authenticated custom authentication token.

---

## One-Time Code Service Requirements

Create a service responsible for one-time codes.

It should support:

```text
generate code
validate code
delete code
check expiration
```

Rules:

- Code length: 6 digits
- Code TTL: 2 minutes
- Code can be used only once
- Generating a new code replaces the old code
- Expired code is invalid
- Code should be deleted after successful use
- Code should not be generated for disabled or locked users

For the first implementation, use in-memory storage.

Example conceptual storage:

```text
username -> code + expiration time
```

Do not use Redis yet unless you want an additional challenge.

---

## Brute-Force Protection Requirements

Implement brute-force protection for both login methods.

### Password Login Blocking

Rules:

```text
3 failed password login attempts -> block password login for 5 minutes
successful password login -> clear failed password attempts
```

### One-Time Code Login Blocking

Rules:

```text
5 failed code login attempts -> block code login for 5 minutes
successful code login -> clear failed code attempts
```

### Login Attempt Service Requirements

Create a service that can:

```text
check if username is blocked for a login method
record successful login
record failed login
clear attempts after success
block user temporarily after threshold
```

Login methods:

```text
PASSWORD
ONE_TIME_CODE
```

Use separate counters for each method.

Example conceptual keys:

```text
login:attempts:password:<username>
login:blocked:password:<username>
login:attempts:code:<username>
login:blocked:code:<username>
```

For the first implementation, use in-memory storage.

Design it in a way that could later be replaced with Redis.

---

## SecurityContext Requirements

After successful authentication, manually create a new security context.

Requirements:

- Do not put unauthenticated authentication objects into the context.
- Create a new empty context.
- Put the authenticated `Authentication` into the context.
- Store the context in `SecurityContextHolder`.
- Save the context using `SecurityContextRepository`.

Conceptual flow:

```text
authenticated Authentication
        -> new SecurityContext
        -> SecurityContextHolder
        -> SecurityContextRepository
        -> HTTP session
```

The goal is to understand how authentication persists between requests.

---

## Session-Based Authentication Requirements

This task should use session-based authentication.

Requirements:

- No JWT.
- No Bearer tokens.
- Authentication should persist through HTTP session.
- After login, the client should be able to call authenticated endpoints using the session cookie.
- After logout, the session authentication should be cleared.

---

## Logout Requirements

Endpoint:

```http
POST /api/v1/auth/logout
```

Logout should:

- Require authentication.
- Clear the security context.
- Invalidate the HTTP session.
- Return a success status without exposing sensitive data.

After logout, the same session should not be able to access authenticated endpoints.

---

## Authorization Requirements

Configure endpoint authorization rules.

Rules:

- Public endpoints are accessible anonymously.
- Authenticated endpoints require any authenticated user.
- Admin endpoints require admin role.

Expected behavior:

| User Type | `/api/v1/users/me` | `/api/v1/resources` | `/api/v1/admin/users` |
|---|---:|---:|---:|
| Anonymous | Denied | Denied | Denied |
| USER | Allowed | Allowed | Denied |
| ADMIN | Allowed | Allowed | Allowed |

---

## Security Configuration Requirements

Your security configuration should include:

- Authorization rules
- Session-based authentication policy
- Logout configuration
- Registration of custom authentication provider
- CORS configuration if needed
- CSRF decision with explanation

Because this is a REST-style app using session authentication, think carefully about CSRF.

If you disable CSRF, write a short comment or note explaining why.

If you keep CSRF enabled, make sure your testing strategy handles CSRF tokens.

---

## Suggested Package Structure

You can organize the project like this:

```text
src/main/java/.../
  security/
    SecurityConfig

    user/
      CustomUserDetails
      CustomUserDetailsService

    code/
      OneTimeCodeAuthenticationToken
      OneTimeCodeAuthenticationProvider
      OneTimeCodeService
      InMemoryOneTimeCodeService

    bruteforce/
      LoginAttemptService
      InMemoryLoginAttemptService
      LoginMethod

  auth/
    AuthController
    AuthService
    PasswordLoginRequest
    CodeLoginRequest
    LoginResponse

  user/
    User
    UserRepository
    UserRole
    UserStatus
    RegisterRequest
    UserResponse

  resource/
    ResourceController

  admin/
    AdminController
```

This structure is only a suggestion. You can adapt it.

---

## Required Manual Flows To Implement

### Password Login Flow

You must be able to explain and demonstrate this flow:

```text
HTTP request with username/password
        -> controller
        -> authentication service
        -> unauthenticated UsernamePasswordAuthenticationToken
        -> AuthenticationManager.authenticate(...)
        -> DaoAuthenticationProvider
        -> UserDetailsService
        -> PasswordEncoder
        -> authenticated Authentication
        -> SecurityContext
        -> SecurityContextHolder
        -> SecurityContextRepository
        -> HTTP session
```

---

### One-Time Code Login Flow

You must be able to explain and demonstrate this flow:

```text
HTTP request with username/code
        -> controller
        -> authentication service
        -> unauthenticated custom Authentication token
        -> AuthenticationManager.authenticate(...)
        -> custom AuthenticationProvider
        -> UserDetailsService
        -> OneTimeCodeService
        -> authenticated custom Authentication token
        -> SecurityContext
        -> SecurityContextHolder
        -> SecurityContextRepository
        -> HTTP session
```

---

### Authenticated Request Flow

You must be able to explain and demonstrate this flow:

```text
HTTP request with session cookie
        -> Spring Security loads SecurityContext from session
        -> SecurityContext contains authenticated Authentication
        -> authorization rules are checked
        -> controller is called if access is allowed
```

---

### Logout Flow

You must be able to explain and demonstrate this flow:

```text
Authenticated logout request
        -> logout filter / logout endpoint
        -> clear SecurityContext
        -> invalidate HTTP session
        -> next request is anonymous
```

---

## Testing Requirements

Write automated tests for the following scenarios.

### Registration Tests

- Registering a user stores an encoded password.
- Registering a duplicate username fails.
- Registration response does not contain password.

### Password Login Tests

- Password login succeeds with correct credentials.
- Password login fails with wrong password.
- Password login fails for disabled user.
- Password login fails for locked user.
- After successful password login, authenticated endpoint is accessible.

### Password Brute-Force Tests

- Failed password login increases attempt count.
- After 3 failed password attempts, password login is blocked.
- Block lasts 5 minutes.
- Successful password login clears failed attempt count.

### One-Time Code Tests

- Code generation creates a 6-digit code.
- Code generation replaces old code.
- Code login succeeds with valid code.
- Code login fails with invalid code.
- Code login fails with expired code.
- Code cannot be reused after successful login.
- Code is not generated for disabled or locked users.

### One-Time Code Brute-Force Tests

- Failed code login increases attempt count.
- After 5 failed code attempts, code login is blocked.
- Block lasts 5 minutes.
- Successful code login clears failed attempt count.

### Authorization Tests

- Anonymous user cannot access `/api/v1/users/me`.
- Authenticated user can access `/api/v1/users/me`.
- USER role cannot access `/api/v1/admin/users`.
- ADMIN role can access `/api/v1/admin/users`.

### Logout Tests

- Authenticated user can logout.
- After logout, authenticated endpoints are denied.
- Session is invalidated after logout.

---

## Manual Testing Checklist

Use Postman, curl, HTTPie, or IntelliJ HTTP client.

### 1. Register User

```http
POST /api/v1/users/register
```

Expected:

```text
User is created.
Password is encoded in DB.
Response does not contain password.
```

### 2. Login With Password

```http
POST /api/v1/auth/login/password
```

Expected:

```text
Login succeeds.
Session cookie is returned.
```

### 3. Access Authenticated Endpoint

```http
GET /api/v1/users/me
```

Expected:

```text
Request succeeds when session cookie is present.
Request fails without session cookie.
```

### 4. Generate Code

```http
POST /api/v1/auth/code/generate
```

Expected:

```text
A 6-digit code is generated and stored temporarily.
```

### 5. Login With Code

```http
POST /api/v1/auth/login/code
```

Expected:

```text
Login succeeds with valid code.
Code cannot be reused.
```

### 6. Logout

```http
POST /api/v1/auth/logout
```

Expected:

```text
Session is invalidated.
Authenticated endpoints are denied afterwards.
```

---

## Learning Questions To Answer After Implementation

After completing the task, answer these questions in your own words:

1. What is the difference between principal and credentials?
2. What is the difference between unauthenticated and authenticated `Authentication`?
3. Why does `AuthenticationManager` return a new authenticated object instead of modifying the old one?
4. What does `ProviderManager` do?
5. How does `ProviderManager` choose an `AuthenticationProvider`?
6. What does `DaoAuthenticationProvider` do?
7. What does `UserDetailsService` do?
8. What does `PasswordEncoder` do?
9. What is the difference between your domain `User` and `UserDetails`?
10. What is stored in `SecurityContext`?
11. What is stored in `SecurityContextHolder`?
12. What does `SecurityContextRepository` do?
13. What is the difference between authentication and authorization?
14. What happens during logout?
15. What would change if this project used JWT instead of sessions?

---

## Constraints

Do not implement JWT in this task.

Do not copy a ready-made authentication implementation from a tutorial.

Do not manually compare passwords in your authentication service.

Do not store raw passwords.

Do not put unauthenticated authentication objects into `SecurityContext`.

Do not ignore account status checks.

Do not mix one-time-code logic into the password provider.

Do not return sensitive fields in API responses.

---

## Optional Advanced Challenges

After the main task works, you can add these features:

1. Replace in-memory one-time-code storage with Redis.
2. Replace in-memory brute-force storage with Redis.
3. Add transaction/request id to logs using MDC.
4. Add custom authentication success/failure handlers.
5. Add audit logs for login success, login failure, logout, and blocked login attempts.
6. Add method-level security with `@PreAuthorize`.
7. Add remember-me authentication and compare it with normal session authentication.
8. Add a second custom provider for API key authentication.
9. Add integration tests with Testcontainers.
10. Add actuator endpoint security rules.

---

## Completion Criteria

You can consider the task complete when:

- Password login works through `AuthenticationManager`.
- One-time-code login works through a custom `AuthenticationProvider`.
- Authenticated users are stored in `SecurityContext`.
- Security context is persisted through HTTP session.
- Logout clears authentication.
- Brute-force protection works for both login methods.
- Authorization rules work correctly.
- Tests cover the main success and failure scenarios.
- You can explain the full authentication flow without looking at code.

