-- Insert clients
INSERT INTO clients (name) VALUES ('Janis Berzins');
INSERT INTO clients (name) VALUES ('Laura Silina');
INSERT INTO clients (name) VALUES ('Anna Koce');
INSERT INTO clients (name) VALUES ('Martins Birgmanis');
INSERT INTO clients (name) VALUES ('Rihards Skuja');
INSERT INTO clients (name) VALUES ('Nikolajs Vasiljevs');
INSERT INTO clients (name) VALUES ('Eva Jansone');
INSERT INTO clients (name) VALUES ('Peteris Krumins');
INSERT INTO clients (name) VALUES ('Olga Pavlova'); -- Client with no accounts

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

-- Martins Birgmanis
INSERT INTO accounts (client_id, currency, balance) VALUES (4, 'GBP', 2000.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (4, 'EUR', 750.00);

-- Rihards Skuja
INSERT INTO accounts (client_id, currency, balance) VALUES (5, 'USD', 2500.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (5, 'CHF', 3000.00);

-- Nikolajs Vasiljevs
INSERT INTO accounts (client_id, currency, balance) VALUES (6, 'JPY', 100000);
INSERT INTO accounts (client_id, currency, balance) VALUES (6, 'EUR', 1500.00);

-- Eva Jansone
INSERT INTO accounts (client_id, currency, balance) VALUES (7, 'USD', 700.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (7, 'AUD', 900.00);

-- Peteris Krumins
INSERT INTO accounts (client_id, currency, balance) VALUES (8, 'CAD', 1200.00);
INSERT INTO accounts (client_id, currency, balance) VALUES (8, 'NZD', 1600.00);

-- Client Rihards Skuja account with many transactions
INSERT INTO accounts (client_id, currency, balance) VALUES (5, 'SEK', 5000.00);

-- Transactions for each account
-- Multiple transactions for Rihards Skuja SEK account
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 100.00, 'SEK', 3);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 150.00, 'SEK', 2);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, -200.00, 'SEK', 1);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 300.00, 'SEK', 4);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, -500.00, 'SEK', 5);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 250.00, 'SEK', 6);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, -100.00, 'SEK', 7);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 200.00, 'SEK', 8);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 350.00, 'SEK', 9);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, -400.00, 'SEK', 11);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (10, 500.00, 'SEK', 12);

-- Additional transactions for diversity
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (1, 450.00, 'USD', 3);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (2, -320.00, 'EUR', 4);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (3, 280.00, 'USD', 1);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (4, -400.00, 'GBP', 2);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (5, 750.00, 'USD', 1);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (6, -500.00, 'EUR', 2);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (7, 600.00, 'USD', 3);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (8, -100.00, 'GBP', 4);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (9, 350.00, 'CHF', 5);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (11, -150.00, 'JPY', 6);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (12, 100.00, 'USD', 7);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (13, -200.00, 'AUD', 8);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (14, 300.00, 'CAD', 5);
INSERT INTO transactions (account_id, amount, currency, transaction_account_id) VALUES (15, -250.00, 'NZD', 10);
