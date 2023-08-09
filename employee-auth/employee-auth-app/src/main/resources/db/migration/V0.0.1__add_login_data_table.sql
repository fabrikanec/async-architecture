create table login_data
(
    id              uuid primary key,
    request_id      uuid         not null,
    request_hash    varchar(128) not null,
    request_data    jsonb        not null,
    valid_from      timestamptz  not null,
    valid_until     timestamptz  not null,
    spent_date_time timestamptz
);

create unique index idx__login_data__request_id
    on login_data (request_id);

create unique index idx__login_data__request_hash
    on login_data (request_hash);
