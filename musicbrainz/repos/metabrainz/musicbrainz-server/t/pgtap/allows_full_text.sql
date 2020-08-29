SET search_path = 'musicbrainz', 'public';

BEGIN;
SELECT no_plan();

--------------------------------------------------------------------------------
-- Test setup. See below for tests.
INSERT INTO work (id, gid, name)
  VALUES (1, '86a36512-88e8-4346-b7bf-1ecbb7b43fb3', 'blah');

-- Ensure that only attribute types that allow text can have text added and that the CHECK constraint works

SELECT lives_ok(
  'INSERT INTO work_attribute (id, work, work_attribute_type, work_attribute_type_allowed_value, work_attribute_text)
   VALUES (1, 1, 6, NULL, ''This type of attribute allows text'')');

SELECT lives_ok(
  'INSERT INTO work_attribute (id, work, work_attribute_type, work_attribute_type_allowed_value, work_attribute_text)
   VALUES (2, 1, 6, 13, NULL)'); -- No text and an allowed_value link


SELECT throws_ok(
  'INSERT INTO work_attribute (id, work, work_attribute_type, work_attribute_type_allowed_value, work_attribute_text)
   VALUES (1, 1, 6, 13, ''This type of attribute allows text but allowed_value is not null'')');

SELECT throws_ok(
  'INSERT INTO work_attribute (id, work, work_attribute_type, work_attribute_type_allowed_value, work_attribute_text)
   VALUES (1, 1, 1, NULL, ''This type of attribute does not allow text'')');

SELECT finish();
ROLLBACK;
