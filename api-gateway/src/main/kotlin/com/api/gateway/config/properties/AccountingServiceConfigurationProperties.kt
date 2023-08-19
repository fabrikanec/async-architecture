package com.api.gateway.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("accounting-service")
@ConstructorBinding
data class AccountingServiceConfigurationProperties(
    val url: String,
)
