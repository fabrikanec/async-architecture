package com.taskmanagement.accounting.payment

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class PaymentHttpDto(
    val id: UUID,
    val incomeAmount: BigDecimal,
    val outcomeAmount: BigDecimal,
    val created: Instant,
)
