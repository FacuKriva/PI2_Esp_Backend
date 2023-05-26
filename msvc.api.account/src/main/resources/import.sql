-- Insert Accounts
INSERT INTO accounts (account_id, cvu, alias, available_balance, user_id) VALUES (null, '1828142364587587491111', 'accion.adeudar.afianzamiento',400000.0, 1);
INSERT INTO accounts (account_id, cvu, alias, available_balance, user_id) VALUES (null, '1828142364587587493333', 'afectacion.divisa.cambios',220000.0, 2);
INSERT INTO accounts (account_id, cvu, alias, available_balance, user_id) VALUES (null, '1828142364587587495555', 'riqueza.dineros.devaluacion',120000.0, 3);

-- Insert Transactions
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 100000.0, '2023-05-10 08:30:20', 'initial transaction', '1828142364587587491111', 1828142364587587493333, 'OUTGOING', 1);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 100000.0, '2023-05-10 08:30:20', 'received transaction', '1828142364587587491111', 1828142364587587493333, 'INCOMING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 20000.0, '2023-05-12 14:30:20', 'payment transaction', '1828142364587587493333', 1828142364587587495555, 'OUTGOING', 2);
INSERT INTO transactions (transaction_id, amount, realization_date, description, from_cvu, to_cvu, type, account_id) VALUES (null, 20000.0, '2023-05-12 14:30:20', 'received transaction', '1828142364587587493333', 1828142364587587495555, 'INCOMING', 3);

