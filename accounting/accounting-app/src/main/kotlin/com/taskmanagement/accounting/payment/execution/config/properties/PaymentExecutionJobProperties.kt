package com.taskmanagement.accounting.payment.execution.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("task-shuffle-job")
data class PaymentExecutionJobProperties(
    val cron: String,
)
