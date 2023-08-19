package com.taskmanagement.accounting.payment.analytics

import com.taskmanagement.employee.EmployeeRole
import com.user.User
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService,
) {

    @GetMapping
    fun today(
        user: User,
    ) {
        require(user.roles.any { it in analyticsPerimitedRoles }) {
            "You are not allowed to view analytics"
        }
    }


    @GetMapping("/tasks/most-expensive")
    fun getMostExpensiveTask(
        user: User,
    ) {
        require(user.roles.any { it in analyticsPerimitedRoles }) {
            "You are not allowed to view analytics"
        }
    }


    companion object {
        private val analyticsPerimitedRoles = setOf(
            EmployeeRole.ADMINISTRATOR.name,
        )
    }
}
