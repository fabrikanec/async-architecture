package com.taskmanagement.accounting.payment.event.flow.v1.mapper

import com.accounting.payment.event.flow.PaymentScheduledEventV1
import com.taskmanagement.accounting.payment.jpa.Payment
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class PaymentFlowEventMapper(
    private val clock: Clock,
) {
    fun Payment.toPaymentScheduledEventV1(): PaymentScheduledEventV1 =
        PaymentScheduledEventV1(
            id = id,
            employeeId = employeeId,
            created = created,
            incomeAmount = income,
            outcomeAmount = outcome,
        )
}
