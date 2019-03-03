DELETE
FROM meals;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (description, calories, user_id)
VALUES ('breakfast', 500, 100000),
       ('dinner', 1000, 100000),
       ('lunch', 1500, 100000),
       ('breakfast', 100, 100001),
       ('dinner', 200, 100001),
       ('lunch', 300, 100001);
