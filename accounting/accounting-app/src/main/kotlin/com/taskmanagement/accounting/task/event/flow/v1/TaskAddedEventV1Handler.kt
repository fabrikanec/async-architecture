package com.taskmanagement.accounting.task.event.flow.v1

import com.taskmanagement.accounting.payment.usecase.ChargeUseCase
import com.taskmanagement.task.event.flow.TaskAddedEventV1
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TaskAddedEventV1Handler(
    private val chargeUseCase: ChargeUseCase,
) {

    @Transactional
    @EventListener
    fun handle(event: TaskAddedEventV1) {
        chargeUseCase(
            employeeId = event.assigneeId,
            taskId = event.id,
        )
    }
}
