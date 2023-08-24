package com.taskmanagement.accounting.payment.schedule.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("payment-job")
data class PaymentScheduleJobProperties(
    val cron: String,
)
