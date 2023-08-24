package com.taskmanagement.accounting.payment.jpa

import java.math.BigDecimal
import java.util.UUID

interface PaymentAggregate {
    val income: BigDecimal
    val outcome: BigDecimal
    val employeeId: UUID
}
