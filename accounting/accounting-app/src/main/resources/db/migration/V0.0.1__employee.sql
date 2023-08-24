create table employee
(
    id      uuid primary key,
    roles   jsonb   not null,
    balance numeric not null,
    created timestamp without time zone not null,
    updated timestamp without time zone not null
);
