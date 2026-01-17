/* =========================================================
   1. Create budget_period table
   ========================================================= */
CREATE TABLE budget_period (
                               id BIGSERIAL PRIMARY KEY,
                               start_date DATE NOT NULL,
                               end_date DATE NOT NULL,
                               CONSTRAINT chk_budget_period_dates
                                   CHECK (start_date <= end_date)
);

/* =========================================================
   2. Insert current active period
   ⚠️ Adjust dates as needed
   ========================================================= */
INSERT INTO budget_period (start_date, end_date)
VALUES ('2026-01-10', '2026-02-09');

/* =========================================================
   3. Remove incorrectly-added date columns from budget_target
   ========================================================= */
ALTER TABLE budget_target_model
    DROP COLUMN IF EXISTS start_date,
    DROP COLUMN IF EXISTS end_date;

/* =========================================================
   4. Add budget_period_id FK column
   ========================================================= */
ALTER TABLE  budget_target_model
    ADD COLUMN budget_period_id BIGINT;

/* =========================================================
   5. Backfill existing budget_target rows
   ========================================================= */
UPDATE  budget_target_model
SET budget_period_id = (
    SELECT id
    FROM budget_period
    ORDER BY id DESC
    LIMIT 1
)
WHERE budget_period_id IS NULL;

/* =========================================================
   6. Enforce NOT NULL + foreign key constraint
   ========================================================= */
ALTER TABLE  budget_target_model
    ALTER COLUMN budget_period_id SET NOT NULL;

ALTER TABLE  budget_target_model
    ADD CONSTRAINT fk_budget_period
        FOREIGN KEY (budget_period_id)
            REFERENCES budget_period (id)
            ON DELETE RESTRICT;

/* =========================================================
   7. Prevent duplicate budgets per bucket per period
   ========================================================= */
CREATE UNIQUE INDEX ux_budget_target_expense
    ON budget_target_model (name, budget_period_id)
    WHERE bucket_model_id IS NULL;

CREATE UNIQUE INDEX ux_budget_target_savings
    ON budget_target_model (bucket_model_id, budget_period_id)
    WHERE bucket_model_id IS NOT NULL;
