package com.taskmanagement.employee.auth.controller

import com.taskmanagement.employee.auth.openapi.SecuredRestController
import com.taskmanagement.employee.auth.token.EmployeeOAuth2TokenSupport
import com.taskmanagement.employee.auth.user.UserApiDto
import com.taskmanagement.employee.jpa.Employee
import com.taskmanagement.employee.jpa.EmployeeRepository
import com.taskmanagement.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@SecuredRestController
@RequestMapping("/users")
class UserController(
    private val employeeOauth2TokenSupport: EmployeeOAuth2TokenSupport,
    private val employeeRepository: EmployeeRepository,
) {

    @GetMapping("/info")
    fun current(): UserApiDto {
        val employee = employeeRepository.getByIdOrThrow(
            id = employeeOauth2TokenSupport.getUserClaimId()
        )
        return employee.toUserApiDto()
    }

    companion object {
        private fun Employee.toUserApiDto(): UserApiDto =
            UserApiDto(
                id = id,
                roles = roles,
            )
    }
}
