package com.accounting.payment.event.flow

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class PaymentScheduledEventV1(
    val id: UUID,
    val employeeId: UUID,
    val created: Instant,
    val incomeAmount: BigDecimal,
    val outcomeAmount: BigDecimal,
) : PaymentFlowEvent
