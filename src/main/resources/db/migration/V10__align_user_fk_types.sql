-- V9__align_user_fk_types.sql
-- Align all *_model.user_id FKs with users.id (BIGINT) and make them NOT NULL

-- 1) Drop existing FK constraints that reference users(id)
ALTER TABLE bank_account_model DROP CONSTRAINT IF EXISTS fk_bank_account_user;
ALTER TABLE bucket_model       DROP CONSTRAINT IF EXISTS fk_bucket_user;
ALTER TABLE transaction_model  DROP CONSTRAINT IF EXISTS fk_transaction_user;
ALTER TABLE budget_target_model DROP CONSTRAINT IF EXISTS fk_budget_target_user;

-- 2) Ensure user_id columns are BIGINT (to match Users.id = Long)
ALTER TABLE bank_account_model
    ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;

ALTER TABLE bucket_model
    ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;

ALTER TABLE transaction_model
    ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;

ALTER TABLE budget_target_model
    ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;

-- 3) Enforce NOT NULL (will fail if any rows have user_id IS NULL)
ALTER TABLE bank_account_model
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE bucket_model
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE transaction_model
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE budget_target_model
    ALTER COLUMN user_id SET NOT NULL;

-- 4) Recreate the FK constraints back to users(id)
ALTER TABLE bank_account_model
    ADD CONSTRAINT fk_bank_account_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE bucket_model
    ADD CONSTRAINT fk_bucket_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE transaction_model
    ADD CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE budget_target_model
    ADD CONSTRAINT fk_budget_target_user
        FOREIGN KEY (user_id) REFERENCES users(id);
