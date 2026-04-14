CREATE TABLE IF NOT EXISTS allowance_matrix (
    id               SERIAL        PRIMARY KEY,
    grade_id         INT           NOT NULL REFERENCES ck_grades(id),
    tier             SMALLINT      NOT NULL CHECK (tier IN (1, 2, 3)),
    da_rate          NUMERIC(10,2) NOT NULL,
    ta_rate          NUMERIC(8,4)  NOT NULL,
    meal_limit       NUMERIC(10,2) NOT NULL DEFAULT 0,
    night_halt_limit NUMERIC(10,2) NOT NULL DEFAULT 0,
    UNIQUE(grade_id, tier)
);
