package com.taskmanagement.tasktracker.employee.event.v1.mapper

import com.taskmanagement.tasktracker.employee.jpa.Employee
import org.springframework.stereotype.Component

@Component
object EmployeeStreamEventMapper {
    fun EmployeeStreamEventV1.toEntity() = Employee(
        id = id,
        roles = roles,
        created = created,
        updated = updated,
    )
}
