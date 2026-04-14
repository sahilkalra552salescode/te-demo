CREATE TABLE IF NOT EXISTS ck_attendance_extension (
    id                    SERIAL           PRIMARY KEY,
    attendance_id         BIGINT           NOT NULL UNIQUE,
    employee_id           BIGINT           NOT NULL,
    odometer_start        NUMERIC(10,2),
    odometer_end          NUMERIC(10,2),
    odometer_start_photo  VARCHAR(500),
    odometer_end_photo    VARCHAR(500),
    odometer_distance     NUMERIC(10,2),
    gps_total_distance    NUMERIC(10,2),
    gps_odometer_variance NUMERIC(8,4),
    variance_status       VARCHAR(20),
    vehicle_used          VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS expenses (
    id                     SERIAL           PRIMARY KEY,
    employee_id            BIGINT           NOT NULL,
    attendance_id          BIGINT           NOT NULL,
    activity_pin_id        BIGINT,
    category               VARCHAR(50)      NOT NULL,
    claimed_amount         NUMERIC(12,2)    NOT NULL,
    ocr_extracted_amount   NUMERIC(12,2),
    bill_photo_url         VARCHAR(500),
    expense_lat            DOUBLE PRECISION,
    expense_lng            DOUBLE PRECISION,
    distance_from_activity NUMERIC(12,2),
    geo_validation_status  VARCHAR(20),
    grade_limit_status     VARCHAR(20),
    submitted_at           TIMESTAMP        NOT NULL DEFAULT NOW(),
    status                 VARCHAR(20)      NOT NULL DEFAULT 'PENDING'
);
