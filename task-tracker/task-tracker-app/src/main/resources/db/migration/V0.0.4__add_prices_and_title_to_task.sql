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
    add column jira_id varchar;
update task
set jira_id = '[DEFAULT]';

alter table task
    alter column jira_id set not null;
