INSERT INTO clients (name) VALUES ('Janis Berzins');
INSERT INTO clients (name) VALUES ('Laura Silina');
INSERT INTO clients (name) VALUES ('Anna Koce');
INSERT INTO clients (name) VALUES ('Matrins Birgmanis');
INSERT INTO clients (name) VALUES ('Rihards Skuja');

-- Insert accounts for each client
-- Janis Berzins
INSERT INTO accounts (client_id, currency, balance) VALUES (1, 'USD', 1000.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (1, 'EUR', 600.00);

-- Laura Silina
INSERT INTO accounts (client_id, currency, balance) VALUES (2, 'USD', 1500.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (2, 'GBP', 800.00);

-- Anna Koce
INSERT INTO accounts (client_id, currency, balance) VALUES (3, 'EUR', 1200.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (3, 'USD', 500.00);

-- Matrins Birgmanis
INSERT INTO accounts (client_id, currency, balance) VALUES (4, 'GBP', 2000.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (4, 'EUR', 750.00);


-- Insert transactions
-- Transactions for Janis Berzins
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (1, 200.00, 'USD', 3);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (1, -150.00, 'USD', 4);

-- Transactions for Laura Silina
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (3, 300.00, 'EUR', 5);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (4, 250.00, 'GBP', 6);

-- Transactions for Anna Koce
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (5, -100.00, 'USD', 1);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (6, 50.00, 'EUR', 2);

-- Transactions for Matrins Birgmanis
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (7, 500.00, 'GBP', 1);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (8, -200.00, 'EUR', 2);