package com.taskmanagement.employee.service

import com.taskmanagement.employee.jpa.Employee
import com.taskmanagement.employee.jpa.EmployeeRepository
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByAuthOrThrow
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
) {

    @Transactional(readOnly = true)
    fun getOne(id: UUID) =
        employeeRepository.getByIdOrThrow(id)

    @Transactional(readOnly = true)
    fun getOneByAuth(login: String, password: String) =
        employeeRepository.getByAuthOrThrow(
            login = login,
            password = password,
        )

    fun register(login: String, password: String) =
        Employee(
            created = Instant.now(),
            login = login,
            password = password,
        ).apply(employeeRepository::save)
}
