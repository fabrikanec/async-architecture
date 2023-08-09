package com.taskmanagement.employee.auth.oauth2

import com.taskmanagement.employee.auth.util.EmployeeAuthServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("${EmployeeAuthServiceProperties.KEY}.oauth2.token")
@ConstructorBinding
data class TokenProperties(
    val validity: TokenValidity = TokenValidity(),
)
