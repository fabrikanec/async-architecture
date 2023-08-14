package com.taskmanagement.employee.auth.user

import com.taskmanagement.employee.EmployeeRole
import java.util.UUID

data class UserApiDto(
    val id: UUID?,
    val roles: Set<EmployeeRole>,
)
