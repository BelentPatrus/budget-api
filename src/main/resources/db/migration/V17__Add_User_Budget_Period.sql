ALTER TABLE public.budget_period
    ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE public.budget_period
    ADD CONSTRAINT fk_budget_period_user
        FOREIGN KEY (user_id) REFERENCES public.users(id)
            ON DELETE CASCADE;

CREATE INDEX idx_budget_period_user_id
    ON public.budget_period(user_id);
