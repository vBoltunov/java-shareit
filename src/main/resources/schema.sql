DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(355) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS items (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    is_available BOOLEAN,
    owner_id BIGINT,
    last_booking_id BIGINT,
    next_booking_id BIGINT
    );

CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    text TEXT,
    author_id BIGINT,
    item_id BIGINT,
    created TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    item_id BIGINT,
    booker_id BIGINT,
    status VARCHAR(255)
);
