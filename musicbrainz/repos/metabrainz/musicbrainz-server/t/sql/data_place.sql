SET client_min_messages TO 'WARNING';

INSERT INTO area (id, gid, name, type) VALUES
  (221, '8a754a16-0027-3a29-b6d7-2b40ea0481ed', 'United Kingdom', 2),
  (222, '489ce91b-6658-3307-9877-795b68554c98', 'United States', 2);
INSERT INTO country_area (area) VALUES (221), (222);
INSERT INTO iso_3166_1 (area, code) VALUES (221, 'GB'), (222, 'US');

INSERT INTO place
    (id, gid, name, type, area, coordinates,
     begin_date_year, begin_date_month, begin_date_day,
     end_date_year, end_date_month, end_date_day, comment,
     last_updated, address)
    VALUES
    (1, '745c079d-374e-4436-9448-da92dedef3ce', 'Test Place', 2, 221,
     '(0.11,0.1)', 2008, 01, 02, 2009, 03, 04, 'Yet Another Test Place',
     '2009-07-09', 'An Address');

INSERT INTO place (id, gid, name)
       VALUES (2, '945c079d-374e-4436-9448-da92dedef3cf', 'Minimal Place'),
              (3, '7d47a33a-bb56-45ae-a479-62e519cf3bd8', 'Minimal Place Mark 2');

INSERT INTO annotation (id, editor, text) VALUES (1, 1, 'Test annotation 1');
INSERT INTO annotation (id, editor, text) VALUES (2, 1, 'Test annotation 2');

INSERT INTO place_annotation (place, annotation) VALUES (1, 1);
INSERT INTO place_annotation (place, annotation) VALUES (2, 2);

INSERT INTO place_gid_redirect VALUES ('a4ef1d08-962e-4dd6-ae14-e42a6a97fc11', 1);
