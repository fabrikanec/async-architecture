package com.taskmanagement.accounting.payment.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Repository
interface PaymentRepository :
    JpaRepository<Payment, UUID>,
    PagingAndSortingRepository<Payment, UUID> {
    @Query(
        """
            select 
             (coalesce(sum(income), 0)
             + 
             coalesce(sum(outcome), 0)) * -1
             from payment
            where created > :fromDate
        """,
        nativeQuery = true,
    )
    fun managementAccrualForToday(fromDate: Instant): BigDecimal

    @Query(
        """
            select 
             coalesce(sum(income), 0 as income
             coalesce(sum(outcome), 0) as outcome
             from payment
             group by employee_id
            where created > :fromDate
        """,
        nativeQuery = true,
    )
    fun findPaymentsAggregateToExecute(fromDate: Instant): List<PaymentAggregate>

    fun findAllByEmployeeId(employeeId: UUID, pageable: Pageable): Page<Payment>
}
