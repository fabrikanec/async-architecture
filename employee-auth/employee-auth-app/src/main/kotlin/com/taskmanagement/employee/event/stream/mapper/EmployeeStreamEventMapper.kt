package com.taskmanagement.employee.event.stream.mapper

import com.taskmanagement.employee.jpa.Employee
import org.springframework.stereotype.Component

@Component
class EmployeeStreamEventMapper {
    fun Employee.toStreamEventV1() = EmployeeStreamEventV1(
        id = id,
        roles = roles,
        created = created,
        updated = updated,
    )
}
