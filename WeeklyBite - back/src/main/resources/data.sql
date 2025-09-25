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


INSERT INTO admin (id) VALUES (1), (2);
INSERT INTO authenticated_user VALUES (3), (4), (5), (6);

INSERT INTO ingredient (id, name, quantity, unit) VALUES
    (1, 'Paradajz', 5, 'kom'),
    (2, 'Šećer', 4, 'kašika'),
    (3, 'Brašno', 5, 'kašičica'),
    (4, 'Mleko', 200, 'ml'),
    (5, 'Jaja', 3, 'kom'),
    (6, 'Meso', 500, 'g'),
    (7, 'Pavlaka', 200, 'ml'),
    (8, 'Sir', 200, 'g'),
    (9, 'Jagode', 150, 'g'),
    (10, 'Šunka', 100, 'g'),
    (11, 'Krompir', 3, 'kom'),
    (12, 'Boranija', 200, 'g'),
    (13, 'Maslinovo ulje', 50, 'ml');

ALTER TABLE recipe ALTER COLUMN description TYPE TEXT;
INSERT INTO recipe (id, created, updated, name, content, description, duration, number_of_people, category, is_deleted, admin_id)
VALUES
    (1, CURRENT_DATE, CURRENT_DATE, 'Pita sa sirom', 'Domaća pita sa sirom i jajima',
      '[{"name": "Fil od sira", "description": "Izmešati sir, jaja i pavlaku"}, {"name": "Priprema kora", "description": "Razviti kore i premažati filom"}]',
      60, 4, 'BREAKFAST', FALSE, 1
    ),
    (2, CURRENT_DATE, CURRENT_DATE, 'Kolač sa jagodama', 'Slatki desert sa jagodama i šlagom',
      '[{"name": "Biskvit", "description": "Umutiti jaja i šećer, dodati brašno"}, {"name": "Fil sa jagodama", "description": "Izmešati mascarpone i jagode, premazati preko biskvita"}]',
      90, 6, 'DESSERT', FALSE, 2
    ),
    (3, CURRENT_DATE, CURRENT_DATE, 'Kolač sa šljivama', 'Slatki desert sa šljivama i šlagom',
      '[{"name": "Biskvit", "description": "Umutiti jaja, šećer i brašno"}, {"name": "Preliv", "description": "Poređati šljive preko biskvita i posuti cimetom"}]',
      120, 4, 'DESSERT', FALSE, 2
    ),
    (4, CURRENT_DATE, CURRENT_DATE, 'Kolač sa makom', 'Slatki desert sa makom i šlagom',
      '[{"name": "Biskvit sa makom", "description": "Umutiti jaja, šećer, brašno i mleveni mak"}, {"name": "Glazura", "description": "Preliti kolač rastopljenim čokoladnim prelivom"}]',
      130, 3, 'DESSERT', FALSE, 2
    ),
    (5, CURRENT_DATE, CURRENT_DATE, 'Omlet sa šunkom', 'Ukusan omlet sa šunkom i sirom',
     '[{"name": "Priprema", "description": "Umutiti jaja, dodati šunku i sir"}]', 15, 2, 'BREAKFAST', FALSE, 1),
    (6, CURRENT_DATE, CURRENT_DATE, 'Palačinke', 'Slatke palačinke sa medom',
     '[{"name": "Priprema", "description": "Pomešati sastojke, ispeći palačinke"}]', 25, 4, 'BREAKFAST', FALSE, 1),
    (7, CURRENT_DATE, CURRENT_DATE, 'Čokoladni kolač', 'Kolač sa čokoladom',
     '[{"name": "Priprema", "description": "Pomešati brašno, šećer i čokoladu"}]', 60, 6, 'DESSERT', FALSE, 2),
    (8, CURRENT_DATE, CURRENT_DATE, 'Pasta sa pavlakom', 'Pasta sa pavlakom i sirom',
     '[{"name": "Priprema", "description": "Skuvati pastu, dodati pavlaku i sir"}]', 30, 3, 'LUNCH', FALSE, 1),
    (9, CURRENT_DATE, CURRENT_DATE, 'Pečeni krompir', 'Pečeni krompir sa maslinovim uljem',
     '[{"name": "Priprema", "description": "Ispeći krompir sa začinima"}]', 40, 4, 'DINNER', FALSE, 1),
    (10, CURRENT_DATE, CURRENT_DATE, 'Čorba od boranije', 'Zdrava čorba od boranije',
     '[{"name": "Priprema", "description": "Skuvati boraniju sa začinima"}]', 35, 4, 'DINNER', FALSE, 2),
    (11, CURRENT_DATE, CURRENT_DATE, 'Salata sa pavlakom', 'Salata sa pavlakom i sirom',
     '[{"name": "Priprema", "description": "Pomešati povrće sa pavlakom"}]', 20, 2, 'LUNCH', FALSE, 1),
    (12, CURRENT_DATE, CURRENT_DATE, 'Krem supa od krompira', 'Krem supa sa krompirom i pavlakom',
     '[{"name": "Priprema", "description": "Skuvati krompir i izmiksati sa pavlakom"}]', 30, 4, 'DINNER', FALSE, 2),
    (13, CURRENT_DATE, CURRENT_DATE, 'Jagodna torta', 'Torta sa jagodama i šlagom',
     '[{"name": "Priprema", "description": "Pomešati brašno, jaja i jagode"}]', 90, 6, 'DESSERT', FALSE, 2),
    (14, CURRENT_DATE, CURRENT_DATE, 'Musaka', 'Musaka sa krompirom i mesom',
     '[{"name": "Priprema", "description": "Ređati slojeve krompira i mesa, peći"}]', 80, 4, 'DINNER', FALSE, 1),
    (15, CURRENT_DATE, CURRENT_DATE, 'Pizza sa šunkom i sirom', 'Pizza sa sirom i šunkom',
     '[{"name": "Priprema", "description": "Razviti testo, staviti sastojke, peći"}]', 50, 4, 'DINNER', FALSE, 1),
    (16, CURRENT_DATE, CURRENT_DATE, 'Čokoladni muffin', 'Muffin sa čokoladom',
     '[{"name": "Priprema", "description": "Pomešati sastojke i peći u kalupima"}]', 25, 6, 'DESSERT', FALSE, 2),
    (17, CURRENT_DATE, CURRENT_DATE, 'Krofne', 'Slatke krofne sa džemom',
     '[{"name": "Priprema", "description": "Zamesiti testo, pržiti krofne"}]', 60, 4, 'DESSERT', FALSE, 2),
    (18, CURRENT_DATE, CURRENT_DATE, 'Pohovani krompir', 'Pohovani krompir sa jajima i brašnom',
     '[{"name": "Priprema", "description": "Uvaljati krompir u brašno i jaja, pržiti"}]', 30, 3, 'DINNER', FALSE, 1),
    (19, CURRENT_DATE, CURRENT_DATE, 'Omlet sa povrćem', 'Omlet sa povrćem i sirom',
     '[{"name": "Priprema", "description": "Pomešati jaja sa povrćem i sirom, peći"}]', 20, 2, 'BREAKFAST', FALSE, 1),
    (20, CURRENT_DATE, CURRENT_DATE, 'Voćna salata', 'Salata sa jagodama i pavlakom',
     '[{"name": "Priprema", "description": "Pomešati jagode sa pavlakom i medom"}]', 15, 2, 'DESSERT', FALSE, 1),
     (21, CURRENT_DATE, CURRENT_DATE, 'Kiflice sa sirom', 'Mekane kiflice sa sirom',
      '[{"name": "Priprema", "description": "Umesiti testo, formirati kiflice i staviti sir unutra, peći 20 minuta"}]',
      30, 4, 'SNACK', FALSE, 1),

     (22, CURRENT_DATE, CURRENT_DATE, 'Štapići sa sezamom', 'Hrskavi štapići sa sezamom',
      '[{"name": "Priprema", "description": "Umesiti testo, oblikovati štapiće, posuti sezamom i peći"}]',
      25, 4, 'SNACK', FALSE, 1);


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

-- Veze recepti - ingredienti (primer, možeš dodati više po receptu)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
    (5, 5), (5, 8), (5, 10),  -- Omlet sa šunkom
    (6, 3), (6, 4), (6, 2),   -- Palačinke
    (7, 2), (7, 3), (7, 5),   -- Čokoladni kolač
    (8, 3), (8, 4), (8, 7),   -- Pasta sa pavlakom
    (9, 11), (9, 13),          -- Pečeni krompir
    (10, 12), (10, 13),        -- Čorba od boranije
    (11, 7), (11, 8),           -- Salata sa pavlakom
    (12, 11), (12, 7),          -- Krem supa od krompira
    (13, 2), (13, 3), (13, 9),  -- Jagodna torta
    (14, 11), (14, 6), (14, 13),-- Musaka
    (15, 3), (15, 8), (15, 10), -- Pizza sa šunkom i sirom
    (16, 2), (16, 3), (16, 5),  -- Čokoladni muffin
    (17, 3), (17, 5),             -- Krofne
    (18, 11), (18, 3), (18, 5),   -- Pohovani krompir
    (19, 11), (19, 8), (19, 7),   -- Omlet sa povrćem
    (20, 9), (20, 7);             -- Voćna salata


-- Veze recepta i sastojaka
INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
    (21, 3), -- Brašno
    (21, 4), -- Mleko
    (21, 5), -- Jaja
    (21, 8), -- Sir
    (22, 3), -- Brašno
    (22, 4), -- Mleko
    (22, 5), -- Jaja
    (22, 13); -- Maslinovo ulje

INSERT INTO recipe_pictures (recipe_id, picture_path) VALUES
    (1, 'pita.png'),
    (1, 'pita2.jpg'),
    (2, 'kolac.jpg'),
    (3, 'kolac_od_sljiva.jpg'),
    (4, 'kolac_od_maka.jpg'),
    (5, 'omlet.jpg'),
    (6, 'palacinke.jpg'),
    (7, 'cokoladni_kolac.jpg'),
    (8, 'pasta.jpg'),
    (9, 'peceni_krompir.jpg'),
    (10, 'corba_boranija.jpg'),
    (11, 'salata.jpg'),
    (12, 'krem_supa.jpg'),
    (13, 'jagodna_torta.jpg'),
    (14, 'musaka.jpg'),
    (15, 'pizza.jpg'),
    (16, 'muffin.jpg'),
    (17, 'krofne.jpg'),
    (18, 'pohovani_krompir.jpg'),
    (19, 'omlet_povrce.jpg'),
    (20, 'vocna_salata.jpeg');

-- Slike novih recepta
INSERT INTO recipe_pictures (recipe_id, picture_path) VALUES
    (21, 'kiflice.jpg'),
    (22, 'stapici_sezam.png');

-- Ubaci sedmice sa eksplicitnim ID-evima
INSERT INTO week (id, start_date, end_date, user_id) VALUES
(1, '2025-09-06', '2025-09-13', 6),
(2, '2025-09-14', '2025-09-21', 6),
(3, '2025-09-22', '2025-09-28', 6),
(4, '2025-09-29', '2025-10-05', 6),
(5, '2025-09-22', '2025-09-28', 7);

-- Ubaci dane sedmica
INSERT INTO week_day (day, week_id) VALUES
('MONDAY', 1), ('TUESDAY', 1), ('WEDNESDAY', 1), ('THURSDAY', 1), ('FRIDAY', 1), ('SATURDAY', 1), ('SUNDAY', 1),
('MONDAY', 2), ('TUESDAY', 2), ('WEDNESDAY', 2), ('THURSDAY', 2), ('FRIDAY', 2), ('SATURDAY', 2), ('SUNDAY', 2),
('MONDAY', 3), ('TUESDAY', 3), ('WEDNESDAY', 3), ('THURSDAY', 3), ('FRIDAY', 3), ('SATURDAY', 3), ('SUNDAY', 3),
('MONDAY', 4), ('TUESDAY', 4), ('WEDNESDAY', 4), ('THURSDAY', 4), ('FRIDAY', 4), ('SATURDAY', 4), ('SUNDAY', 4),
('MONDAY', 5), ('TUESDAY', 5), ('WEDNESDAY', 5), ('THURSDAY', 5), ('FRIDAY', 5), ('SATURDAY', 5), ('SUNDAY', 5);

-- Ubaci recepte
INSERT INTO week_day_recipes (week_day_id, recipe_id) VALUES
(1, 1), (2, 2), (3, 1), (5, 2), (7, 1), (9, 2), (15, 1), (17, 2), (18, 1), (19, 2), (20, 1), (14, 2), (24, 1), (22, 2) ;


SELECT setval('user_account_id_seq', (SELECT MAX(id) FROM user_account));
SELECT setval('person_id_seq', (SELECT MAX(id) FROM person));
SELECT setval('comment_id_seq', (SELECT MAX(id) FROM comment));
SELECT setval('recipe_id_seq', (SELECT MAX(id) FROM recipe));
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));
SELECT setval('week_id_seq', (SELECT MAX(id) FROM week));
SELECT setval('week_day_id_seq', (SELECT MAX(id) FROM week_day));