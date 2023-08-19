package com.taskmanagement.accounting.payment.execution.job

import com.taskmanagement.accounting.payment.execution.PaymentExecutionService
import org.slf4j.info
import org.slf4j.lazyLogger
import org.slf4j.trace
import org.springframework.stereotype.Component

@Component
class PaymentExecutionJob(
    private val paymentExecutionService: PaymentExecutionService,
) {

    private val log by lazyLogger(this::class)

    fun run() {
        log.info { "Running: ${this::class.simpleName}" }
        paymentExecutionService.executePayment()
        log.trace { "Payment execution finished" }
    }
}
