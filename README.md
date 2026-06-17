# Retail-Service

##  Overview
The **Retail Rewards Microservice** is a Spring Boot application that calculates customer reward points based on their transactions.  
It processes transaction data for last 3‑months period, computes monthly reward points, and provides a total summary per customer in paginated format.

This service is designed to demonstrate how to build a RESTful microservice with proper exception handling, modular design, and clean code practices.

Customers earn:
- 2 points for every dollar spent over 100
- 1 point for every dollar spent between 50 and 100
- no points for spending < 50

---

## Features
- Calculates **monthly reward points**
- Calculates **total reward points**
- Supports **multiple customers & transactions**
- Supports **Pagination**
- Provides **RESTful APIs**
- Includes **exception handling**
- Includes **unit & integration testing**

---

## Tech Stack
- **Java 17+**
- **Spring Boot**
- **Maven**
- **REST API**
- **JUnit & Mockito** for testing

---

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/PrasanthiKolli/Retail-Service.git
   cd Retail-Service
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the service at:
   ```bash
   http://localhost:8082
   ```
---
## API Endpoints

- GET /rewards?page=4&size=2 → Fetch rewards for all customers based on page number and page size in pagination format.
- GET /rewards/{customerId} → Fetch rewards for a specific customer.

## Example Response: 

**Request:**
GET http://localhost:8082/rewards?page=4&size=2

**Response Status:**
200 OK
```json
{
  "customerList": [
    {
      "customerId": 1,
      "customerName": "Alice Johnson",
      "monthlyRewards": [
        {
          "yearMonth": "2026-June",
          "points": 180
        }
      ],
      "totalPoints": 180
    },
    {
      "customerId": 2,
      "customerName": "Bob Smith",
      "monthlyRewards": [
        {
          "yearMonth": "2026-April",
          "points": 150
        },
        {
          "yearMonth": "2026-June",
          "points": 250
        }
      ],
      "totalPoints": 400
    }
  ],
  "currentPage": 1,
  "pageSize": 2,
  "totalPages": 5,
  "totalElements": 10
}
```
### Pagination Details

- `customerList` → List of customers with reward details
- `currentPage` → Current page number
- `pageSize` → Number of records per page
- `totalPages` → Total number of pages available
- `totalElements` → Total number of records
---
### GET /rewards/{customerId}

**Description:**  
Fetch reward points for a specific customer.

**Request:**  
GET http://localhost:8082/rewards/1

**Response:**  
Status: 200 OK

```json
{
  "customerId": 1,
  "customerName": "Alice Johnson",
  "monthlyRewards": [
    {
      "yearMonth": "2026-June",
      "points": 180
    }
  ],
  "totalPoints": 180
}
```
---
#  Error Response Example:
**Scenario:** Invalid page number (out of bound)
**Request:** 
GET http://localhost:8082/rewards/12
**Response:**
Status: 400 BAD_REQUEST
```json
{
  "status": 404,
  "message": "Customer with id 12 not found ",
  "timestamp": "2026-06-17T17:35:58.8706751"
}
```
---
## Exception Handling:

    400 BAD_REQUEST → InvalidTransactionException → Invalid transaction.
    404 NOT_FOUND → CustomerDataNotFoundException → Customer data not found.
    400 BAD_REQUEST → PageNumberOutOfBoundException → page number is out of bound
    400 BAD_REQUEST → ConstraintViolationException → invalid input
    500 INTERNAL_SERVER_ERROR → Exception → Generic error.
---
##  Project Structure

- **Controller:** [RewardsController](https://github.com/PrasanthiKolli/Retail-Service/blob/main/src/main/java/com/retail/rewards/controller/RetailerController.java)

- **Service:** [RewardsService](https://github.com/PrasanthiKolli/Retail-Service/blob/main/src/main/java/com/retail/rewards/service/impl/RetailerServiceImpl.java)

- **Utility:** [RetailerUtil](https://github.com/PrasanthiKolli/Retail-Service/blob/main/src/main/java/com/retail/rewards/util/RetailerUtil.java)

- **Entities:** [Entity package](https://github.com/PrasanthiKolli/Retail-Service/tree/main/src/main/java/com/retail/rewards/entity)

- **DTOs:** [DTO package](https://github.com/PrasanthiKolli/Retail-Service/tree/main/src/main/java/com/retail/rewards/dto)

- **Exception:** [Exception package](https://github.com/PrasanthiKolli/Retail-Service/tree/main/src/main/java/com/retail/rewards/exception)

- **Repository:** [Repository package](https://github.com/PrasanthiKolli/Retail-Service/tree/main/src/main/java/com/retail/rewards/repository)

- **Tests:** [Test Classes](https://github.com/PrasanthiKolli/Retail-Service/tree/main/src/test)
  
---
## Database (H2)

This application uses an in-memory H2 database.

Access H2 Console:http://localhost:8082/h2-console
Configuration:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Sample Data

Initial data is loaded using:
- `schema.sql` → Database structure
- `data.sql` → Test data

This ensures consistent setup across environments.

## Author

Developed by Prasanthi Kolli
