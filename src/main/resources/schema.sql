-- Unique loan per active book (partial index)
CREATE UNIQUE INDEX IF NOT EXISTS uq_active_loan_per_book ON loans(book_id) WHERE returned_at IS NULL;