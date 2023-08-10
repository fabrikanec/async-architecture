create table employee
(
    id       uuid primary key,
    roles    jsonb not null,
    username    varchar,
    password varchar,
    created  timestamp without time zone not null,
    updated  timestamp without time zone not null
);
