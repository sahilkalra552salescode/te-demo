CREATE TABLE IF NOT EXISTS city_tiers (
    id        SERIAL       PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL UNIQUE,
    tier      SMALLINT     NOT NULL CHECK (tier IN (1, 2, 3))
);
