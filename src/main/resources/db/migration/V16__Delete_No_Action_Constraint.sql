-- Drop duplicate NO ACTION FKs to users (keep the *_user_id_fkey CASCADE ones)

ALTER TABLE public.bank_account_model
DROP CONSTRAINT IF EXISTS fk_bank_account_user;

ALTER TABLE public.bucket_model
DROP CONSTRAINT IF EXISTS fk_bucket_user;

ALTER TABLE public.budget_target_model
DROP CONSTRAINT IF EXISTS fk_budget_target_user;

ALTER TABLE public.transaction_model
DROP CONSTRAINT IF EXISTS fk_transaction_user;
