DELETE FROM bank_account_model a
    USING bank_account_model b
WHERE a.id > b.id
  AND a.user_id = b.user_id
  AND a.name = b.name;


-- V6__add_unique_bank_account_per_user.sql

ALTER TABLE bank_account_model
    ADD CONSTRAINT uq_bank_account_user_name
        UNIQUE (user_id, name);

