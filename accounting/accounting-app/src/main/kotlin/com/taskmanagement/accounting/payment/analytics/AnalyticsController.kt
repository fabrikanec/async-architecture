package com.taskmanagement.accounting.payment.analytics

import com.taskmanagement.accounting.analytics.AnalyticsTodayStatisticsResponseDto
import com.taskmanagement.accounting.analytics.MostExpensiveTaskPeriod
import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.employee.EmployeeRole
import com.user.User
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
    ): AnalyticsTodayStatisticsResponseDto {
        require(user.roles.any { it in analyticsPerimitedRoles }) {
            "You are not allowed to view analytics"
        }
        return AnalyticsTodayStatisticsResponseDto(
            managementAccrualAmount = analyticsService.getManagementAccrualForToday(),
            negativeBalanceCount = analyticsService.getNegativeBalanceCount(),
        )
    }

    @GetMapping("/tasks/most-expensive")
    fun getMostExpensiveTask(
        period: MostExpensiveTaskPeriod,
        user: User,
    ): Task? {
        require(user.roles.any { it in analyticsPerimitedRoles }) {
            "You are not allowed to view analytics"
        }
        // TODO null to 202 response
        return analyticsService.findMostExpensiveTask(period = period)
    }

    companion object {
        private val analyticsPerimitedRoles = setOf(
            EmployeeRole.ADMINISTRATOR.name,
        )
    }
}
