--liquibase formatted sql

--changeset fyodor:init_tables
CREATE TABLE IF NOT EXISTS players(
    id BIGINT PRIMARY KEY,
    state VARCHAR(255),
    plays_with VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sessions(
    session_id VARCHAR(255) PRIMARY KEY,
    host_id BIGINT REFERENCES players (id),
    client_id BIGINT,
    move INTEGER NOT NULL DEFAULT 0,
    field VARCHAR(255) NOT NULL
);

--rollback DROP TABLE sessions;
--rollback DROP TABLE players;
