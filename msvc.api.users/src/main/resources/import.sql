-- Insert Roles
INSERT INTO roles (role_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (role_id, name) VALUES (2, 'ROLE_USER');
-- Insert Usuario
INSERT INTO users (user_id, name, last_name, cvu, alias, dni, email, password, phone, enabled, attempts, verified, role_id) VALUES (null, 'Admin', 'Admin', '5795826983786046299727', 'Jabón.Celular.Carne', 123456789, 'admin@mail.com', '$2a$12$x1tssYTJ/CYjl1JmGeek/.OSxBkm4NOQHltYsYUISETBqqUvGWfhO', 123456789, true, 0, true, 1);
-- Insert Card
INSERT INTO card (card_id, alias, card_number, card_holder, expiration_date, cvv, bank, card_type, is_enabled, user_id) VALUES (null, 'Tarjeta MP', 5412873403403000, 'Admin Admin', '05/2024', 480, 'Mercado Pago', 'Crédito', true, 1);
INSERT INTO card (card_id, alias, card_number, card_holder, expiration_date, cvv, bank, card_type, is_enabled, user_id) VALUES (null, 'Amex Platinum', 376455323736878, 'Admin Admin', '03/2025', 1698, 'American Express Argentina', 'Crédito', true, 1);
