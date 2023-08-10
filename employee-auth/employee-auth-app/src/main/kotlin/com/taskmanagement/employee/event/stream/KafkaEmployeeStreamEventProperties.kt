package com.taskmanagement.employee.event.stream

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("employee.kafka.stream")
@ConstructorBinding
data class KafkaEmployeeStreamEventProperties(
    val topic: String,
)
