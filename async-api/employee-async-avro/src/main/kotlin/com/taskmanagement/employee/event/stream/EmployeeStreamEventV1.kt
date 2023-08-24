package com.taskmanagement.employee.event.stream

import com.taskmanagement.employee.EmployeeRole
import java.time.Instant
import java.util.UUID

data class EmployeeStreamEventV1(
    val id: UUID,
    val roles: MutableSet<EmployeeRole>,
    val created: Instant,
    val updated: Instant,
) : EmployeeStreamEvent
