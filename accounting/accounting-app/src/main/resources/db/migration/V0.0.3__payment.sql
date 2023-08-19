create table payment
(
    id          uuid primary key,
    employee_id uuid    not null,
    income      numeric not null,
    outcome     numeric not null,
    description varchar not null,
    status      varchar not null,
    create_date timestamp with time zone,
    CONSTRAINT operation_history_on_employee FOREIGN KEY (employee_id) REFERENCES employee (id)
);
