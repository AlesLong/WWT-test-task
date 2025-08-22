Test Task Project
This project consists of two microservices (auth-api and data-api) and a PostgreSQL database.
The goal is to demonstrate authentication with JWT, internal service-to-service communication, and a simple data transformation.
Tech Stack
• Java 17

    • Spring Boot (Web, Security, Data JPA)

    • PostgreSQL

    • Docker + Docker Compose

    • PowerShell / curl for testing
Project Description
1. auth-api
◦ Responsible for user registration and login.

        ◦ Issues JWT tokens upon successful authentication.

        ◦ Provides a /process endpoint that requires a valid JWT token.

        ◦ Internally calls the data-api service to transform input text.
2. data-api
    ◦ Internal service, not exposed to external clients.

    ◦ Validates requests with a static header token X-Internal-Token.

    ◦ Provides a /transform endpoint which reverses the input string and converts it to uppercase.
3. PostgreSQL
    ◦ Stores registered users. 

API Endpoints
   Auth API (http://localhost:8080)
   • POST /api/register
   Register a new user. Requires JSON body:
   • { "username": "email", "password": "pass" }
   • POST /api/login
   Authenticate user and return a JWT token. Requires JSON body:
   • { "username": "email", "password": "pass" }
   • POST /api/process
   Process input text using data-api. Requires Authorization header:
   • Authorization: Bearer <jwt_token>
   • JSON body example:
   • { "text": "hello" }
   
   Data API (http://localhost:8081)
   • POST /api/transform
   Internal-only endpoint. Requires header:
   • X-Internal-Token: secret123
   • JSON body example:
   • { "text": "hello" }

How to Run
1. Build and start all services:
   Before commands change user and password in application properties and
   docker-compose.yml
   mvn -f auth-api/pom.xml clean package -DskipTests
   mvn -f data-api/pom.xml clean package -DskipTests 
   docker-compose up --build
2. Check that services are running:
◦ auth-api →http://localhost:8080
  data-api → http://localhost:8081
  PostgreSQL → localhost:5432