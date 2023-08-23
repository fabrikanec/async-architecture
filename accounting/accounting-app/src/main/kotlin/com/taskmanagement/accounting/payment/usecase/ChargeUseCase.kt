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
class ChargeUseCase(
    private val taskRepository: TaskRepository,
    private val paymentRepository: PaymentRepository,
    private val employeeRepository: EmployeeRepository,
    private val clock: Clock,
) {
    @Transactional
    operator fun invoke(
        employeeId: UUID,
        taskId: UUID,
    ) {
        val existingTask: Task = taskRepository.getByIdOrThrow(taskId) ?: TODO("move event to dql")
        val employee = employeeRepository.getByIdOrThrow(employeeId)
        paymentRepository.save(
            Payment(
                employeeId = employeeId,
                created = clock.instant(),
                outcome = existingTask.priceToCharge,
                income = BigDecimal.ZERO,
                description = existingTask.description,
            )
        )
        employee.balance -= existingTask.priceToCharge
        employeeRepository.save(employee)
    }
}
