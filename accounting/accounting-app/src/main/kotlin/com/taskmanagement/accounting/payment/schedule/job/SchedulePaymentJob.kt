package com.taskmanagement.accounting.payment.schedule.job

import com.taskmanagement.accounting.payment.usecase.SchedulePaymentUseCase
import org.slf4j.info
import org.slf4j.lazyLogger
import org.slf4j.trace
import org.springframework.stereotype.Component

@Component
class SchedulePaymentJob(
    private val schedulePaymentUseCase: SchedulePaymentUseCase,
) {

    private val log by lazyLogger(this::class)

    fun run() {
        log.info { "Running: ${this::class.simpleName}" }
        schedulePaymentUseCase()
        log.trace { "Payment schedule finished" }
    }
}
