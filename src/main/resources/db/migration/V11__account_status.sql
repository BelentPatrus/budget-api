ALTER TABLE bank_account_model
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE bank_account_model
    ADD CONSTRAINT bank_account_status_chk
        CHECK (status IN ('ACTIVE', 'ARCHIVED'));

CREATE INDEX idx_bank_account_user_status
    ON bank_account_model (user_id, status);