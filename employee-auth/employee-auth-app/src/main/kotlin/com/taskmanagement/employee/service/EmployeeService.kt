package com.taskmanagement.employee.service

import com.taskmanagement.employee.auth.jwt.EmployeePrincipal
import com.taskmanagement.employee.jpa.Employee
import com.taskmanagement.employee.jpa.EmployeeRepository
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByUsernameOrThrow
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import org.springframework.security.core.authority.AuthorityUtils
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
) : UserDetailsService {

    @Transactional(readOnly = true)
    fun getOne(id: UUID) =
        employeeRepository.getByIdOrThrow(id)

    @Transactional(readOnly = true)
    fun getOneByAuth(login: String) =
        employeeRepository.getByUsernameOrThrow(
            username = login,
        )

    fun register(login: String, password: String) =
        Employee(
            created = Instant.now(),
            username = login,
            password = password,
        ).apply(employeeRepository::save)

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
