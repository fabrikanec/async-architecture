package com.api.gateway.util

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
