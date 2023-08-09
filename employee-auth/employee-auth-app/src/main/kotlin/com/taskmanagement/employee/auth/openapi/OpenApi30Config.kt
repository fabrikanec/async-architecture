package com.taskmanagement.employee.auth.openapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(info = Info(title = "Employee Auth Api", version = "v1"))
@SecurityScheme(
    name = SecuritySchemeNames.BEARER_AUTH,
    type = SecuritySchemeType.HTTP,
    bearerFormat = BearerFormats.JWT,
    scheme = SecuritySchemes.BEARER,
)
class OpenApi30Config
