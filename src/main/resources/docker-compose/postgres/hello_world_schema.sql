CREATE SCHEMA IF NOT EXISTS hello_world_schema;
SET search_path TO hello_world_schema;
CREATE TABLE IF NOT EXISTS hello_world_table (
                                                 id SERIAL PRIMARY KEY,
                                                 message VARCHAR(255) NOT NULL
    );
INSERT INTO hello_world_table (message) VALUES ('Hello, World!');
