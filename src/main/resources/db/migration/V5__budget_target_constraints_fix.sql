-- V5__budget_target_constraints_fix.sql

-- 1. Drop the old unique constraint
ALTER TABLE budget_target_model
    DROP CONSTRAINT IF EXISTS uq_budget_target_user_type_name;

-- 2. Enforce bucket-only logic
ALTER TABLE budget_target_model
    ADD CONSTRAINT chk_budget_target_bucket_required
        CHECK (
            (type = 'BUCKET' AND bucket_model_id IS NOT NULL)
                OR (type <> 'BUCKET' AND bucket_model_id IS NULL)
            );

-- 3. One BUCKET target per bucket per user
CREATE UNIQUE INDEX IF NOT EXISTS uq_budget_target_bucket_per_user
    ON budget_target_model (user_id, bucket_model_id)
    WHERE type = 'BUCKET';

-- 4. Non-bucket targets unique by name
CREATE UNIQUE INDEX IF NOT EXISTS uq_budget_target_name_per_user
    ON budget_target_model (user_id, type, name)
    WHERE type <> 'BUCKET';
