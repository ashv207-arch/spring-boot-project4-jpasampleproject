# Spring Boot JPA CRUD Sample Project

This project demonstrates a complete JPA-based CRUD application using Spring Boot and Hibernate ORM.

## Features Implemented

- **Entity mapping** with JPA annotations (`@Entity`, `@Table`, `@Id`, `@Column`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`).
- **Relationships**:
  - One-to-many: `Department -> Student`
  - Many-to-many: `Student <-> Course`
- **CRUD operations** for students with service-level transaction handling.
- **Automatic primary key generation** using `GenerationType.IDENTITY`.
- **Read patterns**:
  - Find by ID
  - JPQL search (`searchByName`)
  - Native SQL query (`findStudentsWithMinimumFees`)
  - Criteria API dynamic filtering (`findByCriteria`)
  - Pagination and sorting for list endpoints
- **Update patterns**:
  - Full update (`PUT`)
  - Partial update (`PATCH`)
- **Delete support**:
  - Delete by ID
  - Cascading delete from `Department` to `Student` via `CascadeType.ALL` and `orphanRemoval = true`
- **Validation & error handling** with Bean Validation (`jakarta.validation`) and global exception handlers.
- **Transaction management** via `@Transactional` (commit/rollback managed by Spring).

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 in-memory database
- Maven

## Run

```bash
mvn spring-boot:run
```

## API Endpoints

### Students

- `POST /api/students`
- `GET /api/students/{id}`
- `GET /api/students?page=0&size=10&sortBy=id&direction=asc&name=alice`
- `GET /api/students/fees?minFees=1000&page=0&size=10`
- `GET /api/students/search/criteria?name=ali&minFees=500`
- `PUT /api/students/{id}`
- `PATCH /api/students/{id}`
- `DELETE /api/students/{id}`

### Departments

- `POST /api/departments`
- `GET /api/departments`
- `DELETE /api/departments/{id}`

### Courses

- `POST /api/courses`
- `GET /api/courses`
- `DELETE /api/courses/{id}`

## Example Student Request

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "fees": 1500,
  "departmentId": 1,
  "courseIds": [1, 2]
}
```
