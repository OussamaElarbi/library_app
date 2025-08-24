# Library Management System API

A Library Management System (LMS) built with Java 17 and Spring Boot. It provides REST APIs to manage books, members, loans, and returns, with robust validation and data integrity.

## Features

- Register and list books.
- Register members.
- Loan a book to a member.
- Return a borrowed book.
- Business rules:
    - Only one active loan per unique ISBN per member.
    - Only one member may hold a specific book copy (book_id) at a time.
    - No two members can share the same email.
    - Book metadata (ISBN/title/author) stored separately to avoid redundancy.
- Strong validation and error handling.
- Configurable for multiple environments (profiles/env vars).
- OpenAPI 3 specification included.

## Technology Stack

- Language: Java 17
- Framework: Spring Boot
- Database: PostgreSQL
- Dependency Management: Maven
- Containerization: Docker & Docker Compose
- API Spec: OpenAPI 3 (openapi.yaml)

## Why PostgreSQL

- Strong ACID compliance to ensure loan/return consistency.
- Excellent Spring Boot integration.
- Advanced indexing for read-heavy operations.
- Scales with partitioning and sharding strategies as the library grows.

## Getting Started

### Prerequisites

- Java 17
- Maven 3.9+
- Docker & Docker Compose

### Build and Run

1) Build the project and generate OpenAPI sources:
```shell script
mvn clean package -DskipTests
```


2) Start the application stack:
```shell script
docker-compose up --build
```


### Stop and Restart

- Stop:
```shell script
# in the running terminal
Ctrl + C
```


- Restart with fresh containers:
```shell script
docker-compose down
docker-compose up --build
```

## API Endpoints

| Action          | Method | Endpoint |
|-----------------|--------|----------|
| List books      | GET    | /books   |
| Register book   | POST   | /books   |
| Register member | POST   | /members |
| Loan book       | POST   | /loans   |
| Return book     | PATCH  | /return  |

- Request/response bodies use JSON.
- Errors return a consistent shape: `{ "errorCode": number, "message": string, "details": [ string ] }`.

## Postman Collection

- A Postman collection is provided to test all endpoints.
- Download it from the `docs/` folder of this repository (e.g., `docs/Collabera Library API.postman_collection.json`).
- In Postman:
    - File → Import → choose the collection JSON.
    - Set a Postman environment variable `baseUrl` (e.g., `http://localhost:8080`), then run the requests.

## Validation and Error Handling

- Member card number must match: `LIB-<YEAR>-<RANDOM_STRING>` (e.g., `LIB-2025-4F7A1B9C`).
- Book registration requires `title`, `author`, `isbn`, and `totalCopies >= 1`.
- Typical responses:
    - 400 Bad Request: validation errors and invalid payloads.
    - 404 Not Found: referenced entities that do not exist.
    - 409 Conflict: business rule violations (e.g., book already loaned).
    - 500 Internal Server Error: unexpected database or server errors.

## Business Rules Mapping

- Multiple copies per ISBN: each copy is a separate row with a unique `book_id`.
- Only one active loan per copy: enforced with a partial unique index on `loans(book_id)` where `returned_at IS NULL`.
- Only one active loan per member per ISBN: enforced in application logic at loan time.

## Schema Summary

- `book_metadata`: `isbn` (PK), `title`, `author`.
    - Stores shared details per ISBN to avoid duplication.
- `books`: `book_id` (PK), `isbn` (FK to `book_metadata.isbn`).
    - Represents physical copies; many copies can reference the same ISBN.
- `members`: `member_id` (PK, identity), `card_number` (unique), `email` (unique), `name`.
    - Index on `card_number` for fast lookup.
- `loans`: `id` (PK, identity), `booked_at` (default `CURRENT_TIMESTAMP`), `returned_at` (nullable), `book_id` (FK), `member_id` (FK).
    - Indexes: `book_id`, `member_id`, `(member_id, returned_at)`, `returned_at`.
    - Partial unique index: `UNIQUE (book_id) WHERE returned_at IS NULL`.

## Assumptions

- A member can borrow multiple books with different ISBNs, but cannot borrow multiple copies of the same ISBN at the same time.
- Two members cannot share the same email.
- `ISBN` uniquely maps to `title + author` and is stored in a dedicated metadata table.
- A member has a human-friendly unique card number like `LIB-2025-4F7A1B9C`.

## Troubleshooting

- If you see errors about duplicate keys in `loans`:
    - Ensure `id` is defined as an identity/auto-increment in the database.
    - Align the identity sequence with the current max ID if needed.
- If `book_id` is null on insert:
    - Ensure `books.book_id` is an identity/auto-increment column and the entity uses `@GeneratedValue`.
- Check container logs:
```shell script
docker-compose logs -f
```

## License
Include your preferred license (e.g., MIT, Apache 2.0) in a `LICENSE` file.
