package com.taskmanagement.accounting.employee.event.v1.mapper

import com.taskmanagement.employee.event.stream.EmployeeStreamEventV1
import com.taskmanagement.accounting.employee.jpa.Employee
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
