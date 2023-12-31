

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(256) NOT NULL,
    requestor_id BIGINT REFERENCES users (id),
    created TIMESTAMP,
    CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(256) NOT NULL,
  is_available BOOLEAN NOT NULL,
  owner_id BIGINT REFERENCES users(id),
  request_id BIGINT REFERENCES requests(id),
  CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item_id BIGINT REFERENCES items(id),
    booker_id BIGINT REFERENCES users(id),
    status VARCHAR (50),
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(100) NOT NULL,
    item_id BIGINT  REFERENCES items (id),
    author_id BIGINT  REFERENCES users (id),
    created TIMESTAMP,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);