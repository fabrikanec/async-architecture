package com.api.gateway.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("employee-auth-service")
@ConstructorBinding
data class EmployeeAuthServiceConfigurationProperties(
    val url: String,
)
