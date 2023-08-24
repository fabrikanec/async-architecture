package com.taskmanagement.accounting.task.jpa

import com.taskmanagement.accounting.payment.jpa.Payment
import org.hibernate.Hibernate
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
open class Task(
    @Id
    open val id: UUID = UUID.randomUUID(),
    open val created: Instant,
    open var assigneeId: UUID,
    open val description: String,
    open var jiraId: String?,
    open val priceToCharge: BigDecimal,
    open val priceToPay: BigDecimal,
) {

    @Enumerated(EnumType.STRING)
    open var status: TaskStatus = TaskStatus.ASSIGNED

    open var updated: Instant = created

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Payment
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        this::class.simpleName + "(id = $id)"

    companion object {
        operator fun invoke(
            id: UUID = UUID.randomUUID(),
            created: Instant,
            updated: Instant,
            assigneeId: UUID,
            description: String,
            status: TaskStatus,
            jiraId: String?,
            priceToPay: BigDecimal,
            priceToCharge: BigDecimal,
        ): Task =
            Task(
                id = id,
                created = created,
                assigneeId = assigneeId,
                description = description,
                jiraId = jiraId,
                priceToCharge = priceToCharge,
                priceToPay = priceToPay,
            ).apply {
                this.status = status
                this.updated = updated
            }
    }
}
