INSERT INTO employees (id, first_name, last_name, phone_number)
VALUES
('mock_employee', 'Ivan', 'Ivanov', '+71234567890'),
('mock_manager', 'John', 'Snow', '+70987654321');

INSERT INTO employees_roles (employee_id, role)
VALUES
('mock_employee', 'ROLE_EMPLOYEE'),
('mock_manager', 'ROLE_MANAGER');

INSERT INTO users_auth (employee_id, login, password)
VALUES
('mock_employee', 'mihail', '12345'),
('mock_manager', 'stepan', '54321');