DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;

-- https://stackoverflow.com/questions/7774644/which-is-better-single-global-sequence-vs-sequence-per-table
DROP SEQUENCE IF EXISTS user_id_seq;
DROP SEQUENCE IF EXISTS restaurant_id_seq;
DROP SEQUENCE IF EXISTS dish_id_seq;

CREATE SEQUENCE user_id_seq;
CREATE SEQUENCE restaurant_id_seq;
CREATE SEQUENCE dish_id_seq;

CREATE TABLE users
(
    id       BIGINT DEFAULT NEXT VALUE FOR user_id_seq NOT NULL PRIMARY KEY,
    email    VARCHAR(255)                              NOT NULL UNIQUE,
    password VARCHAR(255)                              NOT NULL,
    role     VARCHAR                                   NOT NULL
);

CREATE TABLE restaurants
(
    id   BIGINT DEFAULT NEXT VALUE FOR restaurant_id_seq NOT NULL PRIMARY KEY,
    name VARCHAR(255)                                    NOT NULL,
    deleted BOOL DEFAULT FALSE
);

CREATE TABLE dishes
(
    id            BIGINT DEFAULT NEXT VALUE FOR dish_id_seq NOT NULL PRIMARY KEY,
    name          VARCHAR(255)                              NOT NULL,
    price         INT                                       NOT NULL,
    restaurant_id BIGINT                                    NOT NULL,
    FOREIGN KEY (restaurant_id) references restaurants (id)
);