CREATE TABLE customers (
            customer_id VARCHAR(50) PRIMARY KEY
);

CREATE TABLE transactions (
          id BIGINT AUTO_INCREMENT PRIMARY KEY,
          amount DECIMAL(15, 2) NOT NULL,
          date DATE NOT NULL,
          customer_id VARCHAR(50),

          CONSTRAINT fk_customer
              FOREIGN KEY (customer_id)
                  REFERENCES customers(customer_id)
                  ON DELETE CASCADE
);
