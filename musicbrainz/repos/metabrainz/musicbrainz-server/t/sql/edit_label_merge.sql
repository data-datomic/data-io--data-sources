SET client_min_messages TO 'warning';

INSERT INTO label (id, gid, name, comment)
    VALUES (2, 'da34a170-7f7f-11de-8a39-0800200c9a66', 'Label', 'Label 2'),
           (3, 'e9f5fc80-7f7f-11de-8a39-0800200c9a66', 'Label', 'Label 3');

INSERT INTO label_ipi (label, ipi) VALUES (2, '00284373936');
INSERT INTO label_isni (label, isni) VALUES (2, '0000000106750994');
