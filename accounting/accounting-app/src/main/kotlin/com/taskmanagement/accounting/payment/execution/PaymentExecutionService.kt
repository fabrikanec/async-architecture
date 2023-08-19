package com.taskmanagement.accounting.payment.execution

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
class PaymentExecutionService(
    private val clock: Clock,
) {
    @Transactional
    fun executePayment() {
        TODO()
    }
}
