create table employee
(
    id       uuid primary key,
    roles    jsonb not null,
    login    varchar,
    password varchar,
    created  timestamp without time zone not null,
    updated  timestamp without time zone not null
);
