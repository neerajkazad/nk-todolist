# Todo List Application - Getting Started Guide

This guide provides step-by-step instructions for setting up and running the Todo List application, which consists of a Spring Boot backend API and an Angular frontend.

## Prerequisites

### Backend (Spring Boot)
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

### Frontend (Angular)
- Node.js (latest LTS version recommended)
- npm (comes with Node.js)
- Angular CLI 18.x

## Step 1: Clone the Repository

If you haven't already, clone the repository to your local machine:

```
git clone <repository-url>
cd nk-todolist
```

## Step 2: Set Up MySQL Database

1. Install MySQL if you haven't already (version 8.0 or higher recommended)

2. Log in to MySQL as root or an administrator user:
   ```
   mysql -u root -p
   ```

3. Create a new database for the application:
   ```
   CREATE DATABASE todolist_db;
   ```

4. (Optional) Create a dedicated user for the application:
   ```
   CREATE USER 'todouser'@'localhost' IDENTIFIED BY 'yourpassword';
   GRANT ALL PRIVILEGES ON todolist_db.* TO 'todouser'@'localhost';
   FLUSH PRIVILEGES;
   ```

5. Update the database configuration in `src/main/resources/application.properties` with your MySQL credentials:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/todolist_db
   spring.datasource.username=root  # or 'todouser' if you created a dedicated user
   spring.datasource.password=yourpassword  # replace with your actual password
   ```

## Step 3: Set Up and Run the Backend (Spring Boot)

1. Navigate to the root directory of the project (where the `pom.xml` file is located):
   ```
   cd /path/to/nk-todolist
   ```

2. Build the application:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

4. The backend API will be available at:
   ```
   http://localhost:8080/api/todos
   ```

5. You can access your MySQL database using tools like MySQL Workbench, phpMyAdmin, or the MySQL command line:
   ```
   mysql -u root -p todolist_db
   ```

   To verify the tables were created, you can run:
   ```
   SHOW TABLES;
   ```

## Step 4: Set Up and Run the Frontend (Angular)

1. Navigate to the frontend directory:
   ```
   cd todo-ui
   ```

2. Install the required dependencies:
   ```
   npm install
   ```

3. Start the development server:
   ```
   ng serve
   ```

4. The frontend application will be available at:
   ```
   http://localhost:4200
   ```

## Step 5: Testing the API

### Using the Angular Frontend

1. Open your browser and navigate to `http://localhost:4200`
2. Use the UI to create, read, update, and delete todo items

### Using API Tools (Postman, cURL, etc.)

You can test the API endpoints directly using tools like Postman or cURL:

#### Get All Todos
```
GET http://localhost:8080/api/todos
```

#### Get a Specific Todo
```
GET http://localhost:8080/api/todos/{id}
```

#### Create a New Todo
```
POST http://localhost:8080/api/todos
Content-Type: application/json

{
  "title": "Sample Todo",
  "completed": false
}
```

#### Update a Todo
```
PUT http://localhost:8080/api/todos/{id}
Content-Type: application/json

{
  "title": "Updated Todo",
  "completed": true
}
```

#### Delete a Todo
```
DELETE http://localhost:8080/api/todos/{id}
```

## Troubleshooting

### Backend Issues

- If you encounter port conflicts, you can change the port in `src/main/resources/application.properties`
- Make sure Java and Maven are properly installed and configured

### Frontend Issues

- If you encounter Angular CLI version issues, make sure your Angular CLI version matches the project requirements
- If you get dependency errors, try running `npm install` again

## Additional Information

- The backend uses a MySQL database, so data will persist between application restarts
- The database schema is automatically created/updated by Hibernate (spring.jpa.hibernate.ddl-auto=update)
- CORS is enabled in the backend to allow requests from the Angular frontend
- The Angular frontend communicates with the backend API at `http://localhost:8080/api/todos`

## Need Help?

If you encounter any issues or have questions, please refer to:
- The main README.md files in the root directory and the `todo-ui` directory
- The official documentation for [Spring Boot](https://spring.io/projects/spring-boot) and [Angular](https://angular.io/docs)
