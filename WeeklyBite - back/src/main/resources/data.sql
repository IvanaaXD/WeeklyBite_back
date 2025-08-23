INSERT INTO user_account (id, email, password, role)
VALUES
    (1, 'admin@example.com', '$2a$12$W4xo5QGdtFXn.NDr0vwES.XUG2UxkyHpP4A.c.homt1JricbiJVHe', 'ADMIN'),
    (2, 'superadmin@example.com', '$2a$12$W4xo5QGdtFXn.NDr0vwES.XUG2UxkyHpP4A.c.homt1JricbiJVHe' , 'ADMIN'),
    (3, 'm@m.m', '$2a$12$MCb/cX7PwfhBOHufN9r4quU.lX3xwfFOSRPNB13f6zfaHl51LMS.W', 'USER'),
    (4, 'ana.anic@example.com', '$2a$12$MCb/cX7PwfhBOHufN9r4quU.lX3xwfFOSRPNB13f6zfaHl51LMS.W', 'USER'),
    (5, 'jovan.jovanovic@example.com', '$2a$12$MCb/cX7PwfhBOHufN9r4quU.lX3xwfFOSRPNB13f6zfaHl51LMS.W', 'USER'),
    (6, 'jovana2.jovanovic@example.com', '$2a$12$2BjbOilpwg2mJ4aowasUTuymYiolF6e5NXo.bImP58h2xcT77opvK', 'USER'),
    (7, 'milan.milanovic@example.com', '$2a$12$2BjbOilpwg2mJ4aowasUTuymYiolF6e5NXo.bImP58h2xcT77opvK', 'USER'),
    (8,  'sandra.sandric@example.com', '$2a$12$2BjbOilpwg2mJ4aowasUTuymYiolF6e5NXo.bImP58h2xcT77opvK',  'USER'),
    (9,  'stefan.stefanovic@example.com', '$2a$12$MCb/cX7PwfhBOHufN9r4quU.lX3xwfFOSRPNB13f6zfaHl51LMS.W', 'USER');

INSERT INTO person (id, first_name, last_name, phone_number, profile_picture, account_id, birth_location)
VALUES
    (1,'Admin', 'Admin', '+381641234567', '../../../assets/images/food.jpg', 1, 'Novi Sad, Serbia'),
    (2,'Admin', 'Superuser', '+381641111111', 'https://example.com/profiles/admin2.jpg',2,'Beograd, Serbia'),
    (3, 'Marko', 'Marković', '+381641234567', 'a6921a31-e067-49ba-9bfd-ac5bde84b1fe.jpg', 3,'Zajecar, Serbia'),
    (4, 'Ana', 'Anić', '+381601234567', 'https://example.com/profiles/ana.jpg', 4,'Kraljevo, Serbia'),
    (5, 'Jovan', 'Jovanović', '+381651234567', 'https://example.com/profiles/jovan.jpg', 5,'Trebinje, BIH'),
    (6, 'Jovana', 'Jovanović', '+381621234567', 'https://example.com/profiles/jovana.jpg', 6 ,'Kraljevo, Serbia'),
    (7, 'Milan', 'Milanović', '+381641111111', 'https://example.com/profiles/milan.jpg', 7, 'Zajecar, Serbia'),
    (8, 'Sandra', 'Sandrić', '+381641234890', 'https://example.com/profiles/sandra.jpg', 8, 'Novi Sad, Serbia'),
    (9, 'Stefan', 'Stefanović', '+3816554890', 'https://example.com/profiles/stefan.jpg', 9, 'Beograd, Serbia');


INSERT INTO ingredient (id, name, quantity, unit) VALUES
    (1, 'Paradajz', 5, 'kom'),
    (2, 'Šećer', 4, 'kašika'),
    (3, 'Brašno', 5, 'kašičica'),
    (4, 'Mleko', 200, 'ml'),
    (5, 'Jaja', 3, 'kom');

INSERT INTO recipe (id, created, updated, name, content, description, duration, number_of_people, category, is_deleted, admin_id)
VALUES
    (1, CURRENT_DATE, CURRENT_DATE, 'Pita sa sirom', 'Domaća pita sa sirom i jajima', '', 60, 4, 'BREAKFAST', FALSE, 1),
    (2, CURRENT_DATE, CURRENT_DATE, 'Kolač sa jagodama', 'Slatki desert sa jagodama i šlagom', '', 90, 6, 'DESSERT', FALSE, 2),
    (3, CURRENT_DATE, CURRENT_DATE, 'Kolač sa sljivama', 'Slatki desert sa sljivama i šlagom', '', 120, 4, 'DESSERT', FALSE, 2),
    (4, CURRENT_DATE, CURRENT_DATE, 'Kolač sa makom', 'Slatki desert sa makom i šlagom', '', 130, 3, 'DESSERT', FALSE, 2);

-- Recept 1: Pita sa sirom
INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
    (1, 3), -- Brašno
    (1, 4), -- Mleko
    (1, 5); -- Jaja

-- Recept 2: Kolač sa jagodama
INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
    (2, 2), -- Šećer
    (2, 3), -- Brašno
    (2, 4), -- Mleko
    (2, 5); -- Jaja

INSERT INTO recipe_pictures (recipe_id, picture_path) VALUES
    (1, 'pita.png'),
    (1, 'pita2.jpg'),
    (2, 'kolac.jpg'),
    (3, 'kolac.jpg'),
    (4, 'kolac.jpg');


SELECT setval('user_account_id_seq', (SELECT MAX(id) FROM user_account));
SELECT setval('person_id_seq', (SELECT MAX(id) FROM person));
SELECT setval('comment_id_seq', (SELECT MAX(id) FROM comment));
SELECT setval('recipe_id_seq', (SELECT MAX(id) FROM recipe));
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));