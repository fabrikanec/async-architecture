create table task
(
    id          uuid primary key,
    description varchar,
    price_to_charge numeric,
    price_to_pay numeric,
    jira_id     varchar,
    status      varchar,
    assignee_id uuid,
    created     timestamp without time zone not null,
    updated     timestamp without time zone not null,
    CONSTRAINT task_on_employee FOREIGN KEY (assignee_id) REFERENCES employee (id)
);
