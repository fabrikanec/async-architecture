package com.taskmanagement.accounting.payment.jpa

import org.hibernate.Hibernate
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
open class Payment(
    @Id
    open val id: UUID = UUID.randomUUID(),
    open val created: Instant,
    open var employeeId: UUID,
    open val income: BigDecimal,
    open val outcome: BigDecimal,
    open val description: String,
) {

    @Enumerated(EnumType.STRING)
    open var status: PaymentStatus = PaymentStatus.PENDING

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Payment
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        this::class.simpleName + "(id = $id)"

}
