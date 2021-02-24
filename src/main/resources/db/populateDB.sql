DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals ( id,datetime, description, calories, iduser)
VALUES (100000,'2015-06-01 14:00:00-06', 'Юзер ланч', '510', 100000),
       (100001,'2015-06-01 21:00:00-06', 'Админ ужин', '1500', 100001);
