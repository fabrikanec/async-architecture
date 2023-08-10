package com.taskmanagement.employee.service

import com.taskmanagement.employee.jpa.Employee
import com.taskmanagement.employee.jpa.EmployeeRepository
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByAuthOrThrow
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import org.springframework.security.authentication.jaas.JaasGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.AbstractPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
): UserDetailsService {

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

    override fun loadUserByUsername(username: String?): UserDetails {
        return User("login", BCryptPasswordEncoder().encode("password"), true, true, true, true,
            AuthorityUtils.createAuthorityList("USER", "write")
        )
    }
}
