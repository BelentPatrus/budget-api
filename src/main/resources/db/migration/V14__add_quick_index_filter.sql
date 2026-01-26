CREATE INDEX idx_tx_user_bucket_date
    ON transaction_model (user_id, bucket_id, date);