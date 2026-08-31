CREATE TABLE jobs(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payload JSONB,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    run_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT check_status CHECK (status IN ('PENDING', 'RUNNING', 'DONE', 'RETRY', 'ERROR', 'DELETED')),
    CONSTRAINT check_retry CHECK(retry_count < max_retries)
);