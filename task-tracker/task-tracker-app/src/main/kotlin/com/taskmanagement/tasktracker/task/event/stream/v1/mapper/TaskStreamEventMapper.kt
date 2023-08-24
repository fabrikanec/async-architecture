package com.taskmanagement.tasktracker.task.event.stream.v1.mapper

import com.taskmanagement.task.event.stream.TaskStreamEventV1
import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.stereotype.Component

@Component
object TaskStreamEventMapper {
    fun Task.toStreamEventV1(): TaskStreamEventV1 =
        TaskStreamEventV1(
            id = id,
            description = description,
            created = created,
            updated = updated,
            status = status.name,
            assigneeId = assignee.id,
            priceToPay = priceToPay,
            priceToCharge = priceToCharge,
        )
}
