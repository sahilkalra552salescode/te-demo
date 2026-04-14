CREATE TABLE IF NOT EXISTS anti_fraud_rules (
    id          SERIAL      PRIMARY KEY,
    description TEXT        NOT NULL,
    condition   VARCHAR(50) NOT NULL,
    rule_type   VARCHAR(20) NOT NULL DEFAULT 'ALWAYS_ON',
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,
    created_by  BIGINT,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS validation_rules (
    id         SERIAL      PRIMARY KEY,
    category   VARCHAR(50) NOT NULL,
    check_type VARCHAR(50) NOT NULL,
    is_active  BOOLEAN     NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS auto_approval_rules (
    id              SERIAL       PRIMARY KEY,
    category        VARCHAR(50)  NOT NULL UNIQUE,
    condition_field VARCHAR(100),
    condition_op    VARCHAR(30),
    threshold_value VARCHAR(100),
    mode            VARCHAR(10)  NOT NULL DEFAULT 'AUTO',
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS expense_frequency_rules (
    id             SERIAL      PRIMARY KEY,
    category       VARCHAR(50) NOT NULL UNIQUE,
    frequency_type VARCHAR(20) NOT NULL CHECK (frequency_type IN ('CUMULATIVE_CAP','MAX_COUNT','UNLIMITED')),
    max_per_day    INT,
    is_active      BOOLEAN     NOT NULL DEFAULT TRUE
);
