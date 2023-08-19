package com.taskmanagement.accounting.payment.execution

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
class PaymentExecutionService {
    @Transactional
    fun executePayment() {
        TODO()
    }
}
