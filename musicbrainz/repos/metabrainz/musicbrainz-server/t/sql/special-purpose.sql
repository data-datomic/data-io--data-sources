SET client_min_messages TO 'WARNING';

INSERT INTO artist (id, gid, name, sort_name, type) VALUES
    (1, '89ad4ac3-39f7-470e-963a-56509c546377', 'Various Artists', 'Various Artists', 3),
    (2, 'c06aa285-520e-40c0-b776-83d2c9e8a6d1', 'Deleted Artist', 'Deleted Artist', 3);

INSERT INTO label (id, gid, name, type) VALUES
    (1, 'f43e252d-9ebf-4e8e-bba8-36d080756cc1', 'Deleted Label', 4);

SET client_min_messages TO 'NOTICE';
