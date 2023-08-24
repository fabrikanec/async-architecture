package com.taskmanagement.accounting.payment.usecase

import com.taskmanagement.accounting.employee.jpa.EmployeeRepository
import com.taskmanagement.accounting.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import com.taskmanagement.accounting.payment.jpa.Payment
import com.taskmanagement.accounting.payment.jpa.PaymentRepository
import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.accounting.task.jpa.TaskRepository
import com.taskmanagement.accounting.task.jpa.TaskRepository.Companion.getByIdOrThrow
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Clock
import java.util.UUID

@Component
class PayUseCase(
    private val taskRepository: TaskRepository,
    private val paymentRepository: PaymentRepository,
    private val employeeRepository: EmployeeRepository,
    private val clock: Clock,
) {
    @Transactional
    operator fun invoke(
        taskId: UUID,
    ) {
        val task: Task = taskRepository.getByIdOrThrow(taskId) ?: TODO("move event to dql")
        val employee = employeeRepository.getByIdOrThrow(task.assigneeId)
        paymentRepository.save(
            Payment(
                employeeId = employee.id,
                created = clock.instant(),
                outcome = BigDecimal.ZERO,
                income = task.priceToPay,
                description = task.description,
            )
        )
        employee.balance += task.priceToCharge
        employeeRepository.save(employee)
    }
}
