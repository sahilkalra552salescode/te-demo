CREATE TABLE IF NOT EXISTS route_matrix (
    id          SERIAL        PRIMARY KEY,
    route_code  VARCHAR(20)   NOT NULL,
    from_town   VARCHAR(100)  NOT NULL,
    to_town     VARCHAR(100)  NOT NULL,
    distance_km NUMERIC(8,2)  NOT NULL,
    grade_id    INT           NOT NULL REFERENCES ck_grades(id),
    ta_per_km   NUMERIC(8,4)  NOT NULL,
    da_per_day  NUMERIC(10,2) NOT NULL,
    nh_per_night NUMERIC(10,2) NOT NULL DEFAULT 0,
    UNIQUE(route_code, grade_id)
);
