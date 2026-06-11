-- Customers
INSERT INTO customers (customer_id) VALUES ('C1');
INSERT INTO customers (customer_id) VALUES ('C2');

-- Transactions for C1
INSERT INTO transactions (amount, date, customer_id)
VALUES (120, '2026-05-10', 'C1');

INSERT INTO transactions (amount, date, customer_id)
VALUES (75, '2026-04-14', 'C1');

INSERT INTO transactions (amount, date, customer_id)
VALUES (200, '2026-03-17', 'C1');

-- Transactions for C2
INSERT INTO transactions (amount, date, customer_id)
VALUES (130, '2026-04-10', 'C2');

INSERT INTO transactions (amount, date, customer_id)
VALUES (140, '2026-04-14', 'C2');

INSERT INTO transactions (amount, date, customer_id)
VALUES (120, '2026-03-17', 'C2');