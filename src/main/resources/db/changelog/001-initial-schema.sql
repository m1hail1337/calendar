CREATE TABLE teams
(
    id         VARCHAR(40) PRIMARY KEY,
    name       VARCHAR(40),
    manager_id VARCHAR(40)
);

CREATE TABLE employees
(
    id           VARCHAR(40) PRIMARY KEY,
    first_name   VARCHAR(40),
    last_name    VARCHAR(40),
    phone_number VARCHAR(12),
    team_id      VARCHAR(40) REFERENCES teams (id)
);

CREATE TABLE tasks
(
    id            SERIAL PRIMARY KEY,
    status        VARCHAR(10),
    message       TEXT,
    employee_id   VARCHAR(40) REFERENCES employees (id),
    team_id       VARCHAR(40) REFERENCES teams (id),
    creation_date TIMESTAMP
);

CREATE TABLE roles
(
    role VARCHAR(20) PRIMARY KEY
);

CREATE TABLE employees_roles
(
    employee_id VARCHAR(40) REFERENCES employees (id) ON DELETE CASCADE,
    role        VARCHAR(20) REFERENCES roles (role),
    PRIMARY KEY (employee_id, role)
);

CREATE TABLE users_auth
(
    employee_id VARCHAR(40) REFERENCES employees (id) ON DELETE CASCADE PRIMARY KEY,
    login       VARCHAR(40) UNIQUE,
    password    VARCHAR(40)
);

CREATE TABLE auth_tokens
(
    token         VARCHAR(2048) PRIMARY KEY,
    type          VARCHAR(40),
    creation_time TIMESTAMP,
    revoked       BOOLEAN,
    user_id       VARCHAR(40) REFERENCES users_auth (employee_id) ON DELETE CASCADE
);
