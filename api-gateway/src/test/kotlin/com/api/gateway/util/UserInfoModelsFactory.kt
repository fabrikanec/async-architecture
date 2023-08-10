package com.api.gateway.util

import com.taskmanagement.employee.EmployeeRole
import com.taskmanagement.employee.auth.user.UserApiDto
import java.util.UUID

fun userInfoResponse(
    id: UUID = UUID.randomUUID(),
    roles: Set<EmployeeRole> = setOf(
        EmployeeRole.COMMON_EMPLOYEE,
    ),
) = UserApiDto(
    id = id,
    roles = roles,
)
