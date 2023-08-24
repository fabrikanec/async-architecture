package com.taskmanagement.accounting.task.event.stream.v1.mapper

import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.accounting.task.jpa.TaskStatus
import com.taskmanagement.task.event.stream.TaskStreamEventV1
import org.springframework.stereotype.Component

@Component
object TaskStreamEventV1Mapper {
    fun TaskStreamEventV1.toEntity(): Task =
        Task(
            id = id,
            description = description,
            created = created,
            updated = updated,
            status = TaskStatus.valueOf(status),
            assigneeId = assigneeId,
            priceToCharge = priceToCharge,
            priceToPay = priceToPay,
            jiraId = null,
        )
}
