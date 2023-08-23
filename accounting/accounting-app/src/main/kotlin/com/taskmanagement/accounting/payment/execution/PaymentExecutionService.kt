package com.taskmanagement.accounting.payment.execution

import com.taskmanagement.accounting.payment.usecase.ExecutePaymentUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentExecutionService(
    private val executePaymentUseCase: ExecutePaymentUseCase,
) {
    @Transactional
    fun executePayment() {
        executePaymentUseCase()
    }
}
