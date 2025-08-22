CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS processing_log (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    input_text TEXT,
    output_text TEXT,
    created_at TIMESTAMP DEFAULT now()
);
