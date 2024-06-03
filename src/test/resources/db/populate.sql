DELETE FROM dishes;
DELETE FROM restaurants;

ALTER SEQUENCE restaurant_id_seq RESTART WITH 1;
ALTER SEQUENCE dish_id_seq RESTART WITH 1;

INSERT INTO restaurants (name)
VALUES ('Meat restaurant'),
       ('Seafood restaurant');

INSERT INTO dishes (name, price, restaurant_id)
VALUES ('Salt-and-Pepper Steak', 50000, 1),
       ('Pork Shoulder Cutlets', 25000, 1),
       ('Chicken Schnitzel', 15000, 1),
       ('Grilled Sliced Brisket', 30000, 1),
       ('Double Mussels & Crab', 45000, 2),
       ('Mussels & Shrimp', 40000, 2),
       ('Sea Bass', 30000, 2),
       ('Salmon', 35000, 2);
