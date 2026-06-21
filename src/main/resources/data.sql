-- Customers
INSERT INTO customers (customer_name) VALUES ('Alice Johnson');
INSERT INTO customers (customer_name) VALUES ('Bob Smith');
INSERT INTO customers (customer_name) VALUES ('Charlie Brown');
INSERT INTO customers (customer_name) VALUES ('David Miller');
INSERT INTO customers (customer_name) VALUES ('Emma Wilson');
INSERT INTO customers (customer_name) VALUES ('Frank Thomas');
INSERT INTO customers (customer_name) VALUES ('Grace Lee');
INSERT INTO customers (customer_name) VALUES ('Henry Clark');
INSERT INTO customers (customer_name) VALUES ('Isabella Davis');
INSERT INTO customers (customer_name) VALUES ('Jack White');
INSERT INTO customers (customer_name) VALUES ('Henry Grace');


-- Transactions for Customer 1
INSERT INTO transactions (amount, date, customer_id)
VALUES (120, CURRENT_DATE - 10, 1);

INSERT INTO transactions (amount, date, customer_id)
VALUES (120.34, CURRENT_DATE - 5, 1);

INSERT INTO transactions (amount, date, customer_id)
VALUES (75, CURRENT_DATE - 135, 1);

INSERT INTO transactions (amount, date, customer_id)
VALUES (250, CURRENT_DATE - 100, 1);

-- Customer 2
INSERT INTO transactions (amount, date, customer_id)
VALUES (180, CURRENT_DATE - 120, 2);

INSERT INTO transactions (amount, date, customer_id)
VALUES (200, CURRENT_DATE - 10, 2);

INSERT INTO transactions (amount, date, customer_id)
VALUES (150, CURRENT_DATE - 60, 2);

-- Customer 3
INSERT INTO transactions (amount, date, customer_id)
VALUES (90, CURRENT_DATE - 3, 3);

INSERT INTO transactions (amount, date, customer_id)
VALUES (80, CURRENT_DATE , 3);

INSERT INTO transactions (amount, date, customer_id)
VALUES (80, CURRENT_DATE-93 , 3);

INSERT INTO transactions (amount, date, customer_id)
VALUES (60, CURRENT_DATE - 25, 3);

-- Customer 4
INSERT INTO transactions (amount, date, customer_id)
VALUES (220, CURRENT_DATE - 7, 4);

INSERT INTO transactions (amount, date, customer_id)
VALUES (85, CURRENT_DATE - 40, 4);

-- Customer 5
INSERT INTO transactions (amount, date, customer_id)
VALUES (110, CURRENT_DATE - 2, 5);

INSERT INTO transactions (amount, date, customer_id)
VALUES (95, CURRENT_DATE - 50, 5);

-- Customer 6
INSERT INTO transactions (amount, date, customer_id)
VALUES (75, CURRENT_DATE - 8, 6);

INSERT INTO transactions (amount, date, customer_id)
VALUES (160, CURRENT_DATE - 70, 6);

-- Customer 7
INSERT INTO transactions (amount, date, customer_id)
VALUES (300, CURRENT_DATE - 1, 7);

INSERT INTO transactions (amount, date, customer_id)
VALUES (120, CURRENT_DATE - 45, 7);

-- Customer 8
INSERT INTO transactions (amount, date, customer_id)
VALUES (45, CURRENT_DATE - 6, 8);

INSERT INTO transactions (amount, date, customer_id)
VALUES (220, CURRENT_DATE - 30, 8);

-- Customer 9
INSERT INTO transactions (amount, date, customer_id)
VALUES (150, CURRENT_DATE - 4, 9);

INSERT INTO transactions (amount, date, customer_id)
VALUES (80, CURRENT_DATE - 155, 9);

INSERT INTO transactions (amount, date, customer_id)
VALUES (120.09999999999999, CURRENT_DATE - 55, 9);

-- Customer 10
INSERT INTO transactions (amount, date, customer_id)
VALUES (130, CURRENT_DATE - 9, 10);

INSERT INTO transactions (amount, date, customer_id)
VALUES (70, CURRENT_DATE - 65, 10);