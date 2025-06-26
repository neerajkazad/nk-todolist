# Todo List Backend API

This is a Spring Boot application that provides a RESTful API for managing todo items.

## Features

- RESTful API for CRUD operations on todo items
- H2 in-memory database for data storage
- Cross-origin resource sharing (CORS) enabled for the Angular frontend

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Setup and Running

1. Build the application:
   ```
   mvn clean install
   ```

2. Run the application:
   ```
   mvn spring-boot:run
   ```

3. The API will be available at:
   ```
   http://localhost:8080/api/todos
   ```

4. Access the H2 database console at:
   ```
   http://localhost:8080/h2-console
   ```
   - JDBC URL: `jdbc:h2:mem:tododb`
   - Username: `sa`
   - Password: (leave empty)

## API Endpoints

- `GET /api/todos` - Get all todos
- `GET /api/todos/{id}` - Get a specific todo by ID
- `POST /api/todos` - Create a new todo
- `PUT /api/todos/{id}` - Update a todo
- `DELETE /api/todos/{id}` - Delete a todo

## Frontend Application

This backend API is designed to work with the Angular frontend application located in the `todo-ui` directory. Make sure to run both applications to use the complete todo list application.