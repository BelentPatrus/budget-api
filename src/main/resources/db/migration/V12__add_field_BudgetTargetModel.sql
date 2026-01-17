ALTER TABLE budget_dev.public.budget_target_model
    ADD COLUMN planned NUMERIC(19, 2),
    ADD COLUMN start_date DATE NOT NULL,
    ADD COLUMN end_date DATE NOT NULL;

ALTER TABLE budget_dev.public.budget_target_model
    ADD CONSTRAINT chk_budget_dates
        CHECK (start_date <= end_date);