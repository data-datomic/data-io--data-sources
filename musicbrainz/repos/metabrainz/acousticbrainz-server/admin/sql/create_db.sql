-- Create the user and the database. Must run as user postgres.

CREATE USER acousticbrainz NOCREATEDB NOSUPERUSER;
CREATE DATABASE acousticbrainz WITH OWNER = acousticbrainz TEMPLATE template0 ENCODING = 'UNICODE';
