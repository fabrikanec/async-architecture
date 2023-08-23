package com.taskmanagement.accounting.payment.usecase

import com.taskmanagement.accounting.payment.event.flow.v1.mapper.PaymentFlowEventMapper
import com.taskmanagement.accounting.payment.jpa.Payment
import com.taskmanagement.accounting.payment.jpa.PaymentAggregate
import com.taskmanagement.accounting.payment.jpa.PaymentRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class ExecutePaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val paymentFlowEventMapper: PaymentFlowEventMapper,
    private val clock: Clock,
) {
    @Transactional
    operator fun invoke() {
        val timeFrom = LocalDate.now().atStartOfDay().minusHours(1)
        val payments = paymentRepository.findPaymentsAggregateToExecute(
            fromDate = timeFrom.toInstant(ZoneOffset.UTC),
        ).map {
            it.toPayment()
        }.apply(paymentRepository::saveAll)

        payments.forEach { payment ->
            with(paymentFlowEventMapper) {
                applicationEventPublisher.publishEvent(payment.toPaymentScheduledEventV1())
            }
        }

        // send amount with email
    }

    private fun PaymentAggregate.toPayment(): Payment =
        Payment(
            income = income,
            outcome = outcome,
            created = clock.instant(),
            description = "Daily payment",
            employeeId = employeeId,
        )
}
