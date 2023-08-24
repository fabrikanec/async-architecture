package com.taskmanagement.accounting.payment.usecase

import com.taskmanagement.accounting.payment.jpa.PaymentRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class GetStatisticsUseCase(
    private val paymentRepository: PaymentRepository,
) {
    operator fun invoke(
        pageable: Pageable,
    ): BigDecimal =
        paymentRepository.managementAccrualForToday(
            fromDate = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC),
        )
}
