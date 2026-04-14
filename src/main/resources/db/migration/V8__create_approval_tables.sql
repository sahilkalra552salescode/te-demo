CREATE TABLE IF NOT EXISTS approval_chains (
    id        SERIAL       PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    category  VARCHAR(50)  NOT NULL,
    grade_id  INT          REFERENCES ck_grades(id),
    is_active BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS approval_chain_steps (
    id             SERIAL      PRIMARY KEY,
    chain_id       INT         NOT NULL REFERENCES approval_chains(id),
    step_order     INT         NOT NULL,
    approver_type  VARCHAR(30) NOT NULL,
    approver_id    BIGINT,
    execution_type VARCHAR(20) NOT NULL DEFAULT 'SEQUENTIAL',
    timeout_hours  INT         NOT NULL DEFAULT 48,
    on_timeout     VARCHAR(20) NOT NULL DEFAULT 'ESCALATE',
    UNIQUE(chain_id, step_order)
);

CREATE TABLE IF NOT EXISTS claim_approvals (
    id             SERIAL      PRIMARY KEY,
    expense_id     INT         NOT NULL REFERENCES expenses(id),
    chain_id       INT         REFERENCES approval_chains(id),
    current_step   INT         NOT NULL DEFAULT 1,
    overall_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
    paid_on        DATE,
    created_at     TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS claim_approval_steps (
    id                SERIAL      PRIMARY KEY,
    claim_approval_id INT         NOT NULL REFERENCES claim_approvals(id),
    step_order        INT         NOT NULL,
    approver_id       BIGINT,
    action            VARCHAR(20),
    remarks           TEXT,
    actioned_at       TIMESTAMP,
    timeout_at        TIMESTAMP
);
