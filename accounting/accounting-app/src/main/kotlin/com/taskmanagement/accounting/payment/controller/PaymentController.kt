package com.taskmanagement.accounting.payment.controller

import com.taskmanagement.employee.EmployeeRole
import com.user.User
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {

    @GetMapping
    fun getAll(
        user: User,
        pageable: Pageable,
    ) =
        paymentService.getAll(
            user = user,
            pageable = pageable,
        ).map { it.toHttpDto() }


    @GetMapping("/statistics")
    fun getStatistics(
        user: User,
        pageable: Pageable,
    ) {
        require(user.roles.any { it in statisticsPerimitedRoles }) {
            "You are not allowed to view statistics"
        }

        paymentService.getStatics(
            user = user,
            pageable = pageable,
        ).map { it.toHttpDto() }
    }


    companion object {
        private val statisticsPerimitedRoles = setOf(
            EmployeeRole.ACCOUNTER.name,
            EmployeeRole.ADMINISTRATOR.name,
        )
    }
}
