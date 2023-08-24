package com.taskmanagement.accounting.payment.event.flow.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("payment-flow")
@ConstructorBinding
data class PaymentFlowEventProperties(
    val topic: String,
)
