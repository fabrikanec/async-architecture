package com.taskmanagement.employee.auth.util

import com.taskmanagement.employee.jpa.Employee
import java.time.Instant

fun employee(
    created: Instant = Instant.now(),
    username: String = "username",
    password: String = "password",
) = Employee(
    created = created,
    username = username,
    password = password,
).apply {
    roles = mutableSetOf(EmployeeRole.COMMON_EMPLOYEE)
}
