package com.taskmanagement.employee.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("security.oauth2.client.keystore")
@ConstructorBinding
data class ClientKeyStoreProperties(
    val path: String? = null,
    val password: String = "password",
    val alias: String = "jwt",
)
