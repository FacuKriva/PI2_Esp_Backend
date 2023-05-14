-- Insert Roles
INSERT INTO roles (role_id, name) VALUES (1, "ROLE_ADMIN");
INSERT INTO roles (role_id, name) VALUES (2, "ROLE_USER");

-- Insert Usuario
INSERT INTO users (user_id, name, last_name, cvu, alias, dni, email, password, phone, enabled, attempts, role_id) VALUES (null, 'Admin', 'Admin', '5795826983786046299727', 'Jabón.Celular.Carne', 123456789, 'admin@mail.com', '$2a$10$0Dnq0cYGjstYyYubRGWOQO4tWLyWRcUsWa1I/ah2wLxaMMmpK9YKm', 123456789, true, 0, 1);