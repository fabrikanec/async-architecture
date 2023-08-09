package com.taskmanagement.employee.auth.token

import com.taskmanagement.employee.EmployeeRole
import java.util.UUID

data class UserClaim(
    val id: UUID?,
    val roles: Set<EmployeeRole>,
)
