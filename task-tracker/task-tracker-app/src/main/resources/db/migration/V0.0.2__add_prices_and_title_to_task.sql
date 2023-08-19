alter table task
    add column price_to_charge numeric,
    add column price_to_pay numeric;

update task
set price_to_charge = 0,
    price_to_pay    = 0;

alter table task
    alter column price_to_charge set not null;
alter table task
    alter column price_to_pay set not null;
---------------------------------------------------

alter table task
    add column title varchar;
update task
set title = '[DEFAULT]',

    alter table task
alter
column title set not null;
