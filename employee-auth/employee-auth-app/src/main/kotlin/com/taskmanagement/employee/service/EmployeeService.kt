package com.taskmanagement.employee.service

import com.taskmanagement.employee.auth.jwt.EmployeePrincipal
import com.taskmanagement.employee.event.stream.mapper.EmployeeStreamEventMapper
import com.taskmanagement.employee.jpa.Employee
import com.taskmanagement.employee.jpa.EmployeeRepository
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByUsernameOrThrow
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val employeeStreamEventMapper: EmployeeStreamEventMapper,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : UserDetailsService {

    @Transactional(readOnly = true)
    fun getOne(id: UUID) =
        employeeRepository.getByIdOrThrow(id)

    @Transactional(readOnly = true)
    fun getOneByAuth(username: String) =
        employeeRepository.getByUsernameOrThrow(
            username = username,
        )

    @Transactional
    fun register(username: String, password: String): Employee {
        val existing = employeeRepository.findByUsername(username = username)
        check(existing == null) {
            "Employee already exists"
        }

        val employee = Employee(
            created = Instant.now(),
            username = username,
            password = password,
        )
        employeeRepository.save(employee)
        with(employeeStreamEventMapper) {
            applicationEventPublisher.publishEvent(employee.toStreamEventV1())
        }
        return employee
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val employee = getOneByAuth(username)
        return with(employee) {
            EmployeePrincipal(
                id = id,
                roles = roles,
                username = username,
                password = BCryptPasswordEncoder().encode(password),
            )
        }
    }
}
