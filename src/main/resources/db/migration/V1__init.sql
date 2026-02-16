CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    social_id   VARCHAR(255) NOT NULL,
    provider    VARCHAR(20)  NOT NULL,
    nickname    VARCHAR(100),
    email       VARCHAR(255),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (social_id, provider)
);

CREATE TABLE daily_records (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users(id),
    date       DATE         NOT NULL,
    content    VARCHAR(100) NOT NULL,
    category   VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, date)
);

CREATE TABLE refresh_tokens (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL REFERENCES users(id),
    token         VARCHAR(512) NOT NULL UNIQUE,
    expires_at    TIMESTAMP    NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);
