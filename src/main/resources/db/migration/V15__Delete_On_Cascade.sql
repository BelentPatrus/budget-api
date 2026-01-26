ALTER TABLE bank_account_model
ADD CONSTRAINT bank_account_model_user_id_fkey
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE bucket_model
ADD CONSTRAINT bucket_model_user_id_fkey
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE transaction_model
ADD CONSTRAINT transaction_model_user_id_fkey
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE budget_target_model
ADD CONSTRAINT budget_target_model_user_id_fkey
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;
