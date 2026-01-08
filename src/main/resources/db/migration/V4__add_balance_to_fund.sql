ALTER TABLE bucket_model
    ADD COLUMN balance NUMERIC(19,2) NOT NULL DEFAULT 0;

ALTER TABLE bank_account_model
    ADD COLUMN credit_or_debit VARCHAR(10) NOT NULL DEFAULT 'DEBIT';

ALTER TABLE bank_account_model
    ADD COLUMN balance NUMERIC(19,2) NOT NULL DEFAULT 0;

ALTER TABLE transaction_model
    RENAME COLUMN income_or_expense TO transaction_type;

ALTER TABLE transaction_model
    DROP CONSTRAINT IF EXISTS transaction_model_income_or_expense_check;

ALTER TABLE transaction_model
    ADD CONSTRAINT transaction_model_transaction_type_check
        CHECK (transaction_type IN ('INCOME', 'EXPENSE', 'TRANSFER'));

CREATE TABLE IF NOT EXISTS budget_target_model (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    bucket_model_id BIGINT NULL,

    CONSTRAINT fk_budget_target_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_budget_target_bucket
        FOREIGN KEY (bucket_model_id)
            REFERENCES bucket_model (id)
            ON DELETE SET NULL,
    CONSTRAINT uq_budget_target_user_type_name
        UNIQUE (user_id, type, name)
);

-- (Optional but recommended) indexes for common joins/filters
CREATE INDEX IF NOT EXISTS ix_budget_target_user_id
    ON budget_target_model (user_id);

CREATE INDEX IF NOT EXISTS ix_budget_target_bucket_model_id
    ON budget_target_model (bucket_model_id);


