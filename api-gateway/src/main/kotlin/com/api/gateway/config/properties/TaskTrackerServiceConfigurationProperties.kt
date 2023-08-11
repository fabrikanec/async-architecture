package com.api.gateway.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("task-tracker-service")
@ConstructorBinding
data class TaskTrackerServiceConfigurationProperties(
    val url: String,
)
