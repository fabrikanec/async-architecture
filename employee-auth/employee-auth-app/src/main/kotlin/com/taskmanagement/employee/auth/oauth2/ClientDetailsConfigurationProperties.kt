package com.taskmanagement.employee.auth.oauth2

import com.taskmanagement.employee.auth.util.EmployeeAuthServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("${EmployeeAuthServiceProperties.KEY}.oauth2")
@ConstructorBinding
data class ClientDetailsConfigurationProperties(
    val clients: MutableMap<String, ClientData> = mutableMapOf(),
) {
    data class ClientData(
        val clientSecret: String,
        val scope: Set<String> = emptySet(),
        val authorizedGrantTypes: Set<String> = emptySet(),
        val registeredRedirectUri: Set<String> = emptySet(),
        val resourceIds: Set<String> = emptySet(),
        val authorities: Set<String> = emptySet(),
        val token: Token = Token(),
    ) {
        data class Token(
            val validity: TokenValidity = TokenValidity(),
        )
    }
}
