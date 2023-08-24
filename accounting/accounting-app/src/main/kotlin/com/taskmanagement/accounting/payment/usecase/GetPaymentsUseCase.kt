package com.taskmanagement.accounting.payment.usecase

import com.taskmanagement.accounting.payment.jpa.Payment
import com.taskmanagement.accounting.payment.jpa.PaymentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GetPaymentsUseCase(
    private val paymentRepository: PaymentRepository,
) {
    operator fun invoke(
        employeeId: UUID,
        pageable: Pageable,
    ): Page<Payment> =
        paymentRepository.findAllByEmployeeId(employeeId = employeeId, pageable = pageable)
}
