package com.taskmanagement.employee.auth.jwt

import com.taskmanagement.employee.service.EmployeeService
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.TokenGranter

class JwtTokenGranterFactory(
    private val employeeService: EmployeeService,
) {
    fun create(endpointsConfigurer: AuthorizationServerEndpointsConfigurer): TokenGranter =
        JwtTokenGranter(
            endpointsConfigurer,
            employeeService,
        )
}
