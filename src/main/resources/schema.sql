
-- delete tables if exists.
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;

--create table for customer
CREATE TABLE customers (
            customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
            customer_name VARCHAR(50) NOT NULL
);

--create table for transactions.
CREATE TABLE transactions (
          id BIGINT AUTO_INCREMENT PRIMARY KEY,
          amount DECIMAL(15, 2) NOT NULL,
          date DATE NOT NULL,
          customer_id BIGINT NOT NULL,

          CONSTRAINT fk_customer
              FOREIGN KEY (customer_id)
                  REFERENCES customers(customer_id)
                  ON DELETE CASCADE
);
