CREATE TABLE IF NOT EXISTS global_config (
    config_key   VARCHAR(100) NOT NULL PRIMARY KEY,
    config_value VARCHAR(255) NOT NULL,
    updated_by   BIGINT,
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);
