package com.taskmanagement.employee.auth.jwt

import com.taskmanagement.employee.EmployeeRole
import java.util.UUID

data class EmployeePrincipal(
    val id: UUID?,
    val roles: Set<EmployeeRole>,
)
