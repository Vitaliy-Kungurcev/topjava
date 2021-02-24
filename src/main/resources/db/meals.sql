DROP TABLE IF EXISTS meals;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE meals
(

    id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    dateTime    TIMESTAMP CONSTRAINT meals_unique_datetime UNIQUE NOT NULL,
    description VARCHAR                         NOT NULL,
    calories    INTEGER                         NOT NULL,
    idUser INTEGER CONSTRAINT  idUser_fk references users(id)
);

