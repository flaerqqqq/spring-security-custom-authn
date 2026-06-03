-- src/test/resources/sql/authentication/active-users.sql

INSERT INTO users (
    id,
    public_id,
    username,
    password,
    status,
    created_at,
    updated_at
)
VALUES
    (
        1,
        '11111111-1111-1111-1111-111111111111',
        'username',
        -- raw password: password
        crypt('password', gen_salt('bf', 10)),
        'ACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        2,
        '22222222-2222-2222-2222-222222222222',
        'admin',
        -- raw password: password
        crypt('password', gen_salt('bf', 10)),
        'ACTIVE',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        3,
        '33333333-3333-3333-3333-333333333333',
        'disabled_user',
        -- raw password: password
        crypt('password', gen_salt('bf', 10)),
        'DISABLED',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );