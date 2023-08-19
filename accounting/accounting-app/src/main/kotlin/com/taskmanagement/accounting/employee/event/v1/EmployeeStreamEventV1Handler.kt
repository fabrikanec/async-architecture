package com.taskmanagement.accounting.employee.event.v1

import com.taskmanagement.employee.event.stream.EmployeeStreamEventV1
import com.taskmanagement.accounting.employee.event.v1.mapper.EmployeeStreamEventMapper
import com.taskmanagement.accounting.employee.jpa.Employee
import com.taskmanagement.accounting.employee.jpa.EmployeeRepository
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EmployeeStreamEventV1Handler(
    private val employeeRepository: EmployeeRepository,
    private val employeeStreamEventMapper: EmployeeStreamEventMapper,
) {

    @Transactional
    @EventListener
    fun handle(event: EmployeeStreamEventV1) {
        val existingEmployee: Employee? = employeeRepository.findByIdOrNull(event.id)
        if (existingEmployee == null || existingEmployee.updated < event.updated)
            with(employeeStreamEventMapper) {
                employeeRepository.save(
                    event.toEntity()
                )
            }
    }
}
