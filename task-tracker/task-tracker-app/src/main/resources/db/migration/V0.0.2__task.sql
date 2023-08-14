create table task
(
    id          uuid primary key,
    description varchar,
    status      varchar,
    employee_id uuid,
    created     timestamp without time zone not null,
    updated     timestamp without time zone not null,
    CONSTRAINT task_on_employee FOREIGN KEY (employee_id) REFERENCES employee (id)
);
