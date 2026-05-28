# Retail-Service

## 📌 Overview
The **Retail Rewards Microservice** is a Spring Boot application that calculates customer reward points based on their transactions.  
It processes transaction data for a 3‑month period, computes monthly reward points, and provides a total summary per customer.  

This service is designed to demonstrate how to build a RESTful microservice with proper exception handling, modular design, and clean code practices.

Customer earn:
- 2 points for every dollar spent over 100
- 1 point for every dollar spent between 50 and 100
- no points for spending < 50

---

## 🚀 Features
- Calculates **monthly reward points**
- Calculates **total reward points**
- Supports **multiple customers & transactions**
- provides **RESTful APIs**
- Includes **exception handling**
- Includes **unit & integration testing**

---

## 🛠️ Tech Stack
- **Java 17+**
- **Spring Boot**
- **Maven**
- **REST API**
- **JUnit** for testing

---

## ⚙️ Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Retail-Service.git
   cd Retail-Service
2. Build the project:
   mvn clean install
3. Run the application:
   mvn spring-boot:run
4. Access the service at:
   http://localhost:8080
   
## API Endpoints
  GET /rewards → Fetch rewards for all customers.
  GET /rewards/{customerId} → Fetch rewards for a specific customer.

## Example Response
  {
  "customerId": "123",
  "monthlyRewards": [
    { "month": "JANUARY", "points": 120 },
    { "month": "FEBRUARY", "points": 90 },
    { "month": "MARCH", "points": 40 }
  ],
  "totalPoints": 250
  }
## Exception Handling
    404 NOT_FOUND → Resource not found.
    
    400 BAD_REQUEST → Invalid transaction.
    
    204 NO_CONTENT → Customer data not available.
    
    500 INTERNAL_SERVER_ERROR → Generic error.
## Project Structure
  src/
   ├── main/java/com/retail/rewards
   │    ├── controller        # REST controllers
   │    ├── service           # Business logic
   |    ├── dto               # Response DTOs
   |    ├── repository        # repositories
   │    ├── model             # models
   │    ├── exception         # Custom exceptions
   │    └── util              # Utility classes
   └── test/java/com/retail/rewards
        └── ...               # Unit tests


