-- V3__bucket_bank_account_fk.sql

-- 0) Delete dependents first (because transaction_model references bucket_model)
DELETE FROM transaction_model;

-- 1) Now delete buckets safely
DELETE FROM bucket_model;

-- 2) Add the new FK column
ALTER TABLE bucket_model
    ADD COLUMN bank_account_id BIGINT NOT NULL;

-- 3) Add FK constraint (bucket -> bank_account)
ALTER TABLE bucket_model
    ADD CONSTRAINT fk_bucket_bank_account
        FOREIGN KEY (bank_account_id)
            REFERENCES bank_account_model(id)
            ON DELETE CASCADE;

-- 4) Index for performance
CREATE INDEX idx_bucket_model_bank_account_id
    ON bucket_model(bank_account_id);

-- 5) Prevent duplicate bucket names within the same bank account
ALTER TABLE bucket_model
    ADD CONSTRAINT uq_bucket_bank_account_name
        UNIQUE (bank_account_id, name);
