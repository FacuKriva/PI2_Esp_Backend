-- Insert Accounts
INSERT INTO accounts (account_id, cvu, alias, available_balance, user_id) VALUES (null, '1828142364587587491111', 'accion.adeudar.afianzamiento',400000.0, 1);
INSERT INTO accounts (account_id, cvu, alias, available_balance, user_id) VALUES (null, '1828142364587587493333', 'afectacion.divisa.cambios',220000.0, 2);
INSERT INTO accounts (account_id, cvu, alias, available_balance, user_id) VALUES (null, '1828142364587587495555', 'riqueza.dineros.devaluacion',120000.0, 3);

-- Insert Transactions
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 100000.0, '2023-05-10 08:30:20', 'initial transaction', '1828142364587587491111', 1828142364587587493333, 'OUTGOING', 1);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 100000.0, '2023-05-10 08:30:20', 'received transaction', '1828142364587587491111', 1828142364587587493333, 'INCOMING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 20000.0, '2023-05-12 14:30:20', 'payment transaction', '1828142364587587493333', 1828142364587587495555, 'OUTGOING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 20000.0, '2023-05-12 14:30:20', 'received transaction', '1828142364587587493333', 1828142364587587495555, 'INCOMING', 3);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 5300.0, '2023-01-01 16:35:10', 'payment transaction', '1828142364587587491111', 1828142364587587493333, 'INCOMING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 17223.0, '2022-07-01 10:02:00', 'payment transaction', '1828142364587587491111', 1828142364587587493333, 'INCOMING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 112000.0, '2023-01-01 17:21:09', 'payment transaction', '1828142364587587495555', 1828142364587587493333, 'INCOMING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 20000.0, '2018-02-09 12:45:00', 'payment transaction', '1828142364587587495555', 1828142364587587493333, 'INCOMING', 2);

-- Insert Card
INSERT INTO cards (card_id, alias, card_number, card_holder, expiration_date, cvv, bank, card_type, is_enabled, user_id) VALUES (null, 'Tarjeta MP', 5412873403403000, 'Admin Admin', '05/2024', 480, 'Mercado Pago', 'Crédito', true, 1);
INSERT INTO cards (card_id, alias, card_number, card_holder, expiration_date, cvv, bank, card_type, is_enabled, user_id) VALUES (null, 'Amex Platinum', 376455323736878, 'Admin Admin', '03/2025', 1698, 'American Express Argentina', 'Crédito', true, 1);
