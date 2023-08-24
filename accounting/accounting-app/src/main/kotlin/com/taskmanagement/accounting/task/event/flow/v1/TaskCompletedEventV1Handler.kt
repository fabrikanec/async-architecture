package com.taskmanagement.accounting.task.event.flow.v1

import com.taskmanagement.accounting.payment.usecase.PayUseCase
import com.taskmanagement.task.event.flow.TaskCompletedEventV1
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TaskCompletedEventV1Handler(
    private val payUseCase: PayUseCase,
) {

    @Transactional
    @EventListener
    fun handle(event: TaskCompletedEventV1) {
        payUseCase(
            taskId = event.id,
        )
    }
}
