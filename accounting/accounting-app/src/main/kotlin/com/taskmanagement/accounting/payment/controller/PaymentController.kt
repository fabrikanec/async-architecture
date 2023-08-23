package com.taskmanagement.accounting.payment.controller

import com.taskmanagement.accounting.payment.PaymentHttpDto
import com.taskmanagement.accounting.payment.jpa.Payment
import com.taskmanagement.accounting.payment.usecase.GetPaymentsUseCase
import com.taskmanagement.accounting.payment.usecase.GetStatisticsUseCase
import com.taskmanagement.employee.EmployeeRole
import com.user.User
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val getPaymentsUseCase: GetPaymentsUseCase,
    private val getStatisticsUseCase: GetStatisticsUseCase,
) {

    @GetMapping
    fun getAll(
        user: User,
        pageable: Pageable,
    ) =
        getPaymentsUseCase(
            employeeId = user.id,
            pageable = pageable,
        ).map { it.toHttpDto() }

    @GetMapping("/statistics")
    fun getStatistics(
        user: User,
        pageable: Pageable,
    ): BigDecimal {
        require(user.roles.any { it in statisticsPerimitedRoles }) {
            "You are not allowed to view statistics"
        }

        return getStatisticsUseCase(
            pageable = pageable,
        )
    }

    companion object {
        private val statisticsPerimitedRoles = setOf(
            EmployeeRole.ACCOUNTER.name,
            EmployeeRole.ADMINISTRATOR.name,
        )

        private fun Payment.toHttpDto(): PaymentHttpDto =
            PaymentHttpDto(
                id = id,
                incomeAmount = income,
                outcomeAmount = outcome,
                created = created,
            )
    }
}
