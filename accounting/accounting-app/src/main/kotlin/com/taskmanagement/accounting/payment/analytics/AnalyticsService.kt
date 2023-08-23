package com.taskmanagement.accounting.payment.analytics

import com.taskmanagement.accounting.analytics.MostExpensiveTaskPeriod
import com.taskmanagement.accounting.employee.jpa.EmployeeRepository
import com.taskmanagement.accounting.payment.jpa.PaymentRepository
import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.accounting.task.jpa.TaskRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Service
class AnalyticsService(
    private val taskRepository: TaskRepository,
    private val paymentRepository: PaymentRepository,
    private val employeeRepository: EmployeeRepository,
    private val clock: Clock,
) {
    fun findMostExpensiveTask(period: MostExpensiveTaskPeriod): Task? =
        taskRepository.findMostExpensiveTask(
            fromDate = clock.instant().minus(
                1,
                when (period) {
                    MostExpensiveTaskPeriod.D -> ChronoUnit.DAYS
                    MostExpensiveTaskPeriod.W -> ChronoUnit.WEEKS
                    MostExpensiveTaskPeriod.M -> ChronoUnit.MONTHS
                }
            )
        )

    fun getManagementAccrualForToday(): BigDecimal =
        paymentRepository.managementAccrualForToday(
            fromDate = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC),
        )

    fun getNegativeBalanceCount(): Int =
        employeeRepository.negativeBalanceCount()
}
