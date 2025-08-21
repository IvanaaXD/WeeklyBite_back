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


SELECT setval('user_account_id_seq', (SELECT MAX(id) FROM user_account));
SELECT setval('person_id_seq', (SELECT MAX(id) FROM person));
--SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));
SELECT setval('comment_id_seq', (SELECT MAX(id) FROM comment))