package com.taskmanagement.employee.auth.controller

import com.taskmanagement.employee.auth.openapi.SecuredRestController
import com.taskmanagement.employee.auth.token.EmployeeOAuth2TokenSupport
import com.taskmanagement.employee.auth.user.UserApiDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@SecuredRestController
@RequestMapping("/users")
class UserController(
    private val employeeOauth2TokenSupport: EmployeeOAuth2TokenSupport,
) {

    @GetMapping("/info")
    fun current(): UserApiDto = employeeOauth2TokenSupport.toUserApiDto()

    companion object {
        private fun EmployeeOAuth2TokenSupport.toUserApiDto(): UserApiDto =
            UserApiDto(
                id = getOrNullUserClaimId(),
                roles = roles,
            )
    }
}
