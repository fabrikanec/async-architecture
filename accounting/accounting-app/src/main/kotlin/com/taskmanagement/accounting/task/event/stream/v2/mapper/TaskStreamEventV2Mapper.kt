package com.taskmanagement.accounting.task.event.stream.v2.mapper

import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.accounting.task.jpa.TaskStatus
import org.springframework.stereotype.Component

@Component
object TaskStreamEventV2Mapper {

    fun TaskStreamEventV2.toEntity(): Task =
        Task(
            id = id,
            description = description,
            created = created,
            updated = updated,
            status = TaskStatus.valueOf(status),
            assigneeId = assigneeId,
            priceToCharge = priceToCharge,
            priceToPay = priceToPay,
            jiraId = jiraId,
        )
}
