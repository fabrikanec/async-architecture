package com.taskmanagement.tasktracker.task.event.flow.v1.mapper

import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
object TaskFlowEventMapper {
    fun Task.toTaskAssignedEventV1(priceAmount: BigInteger): TaskAssignedEventV1 =
        TaskAssignedEventV1(
            id = id,
            assignee = assignee.id,
            description = description,
            created = created,
            priceAmount = priceAmount
        )

    fun Task.toTaskCompletedEventV1(priceAmount: BigInteger): TaskCompletedEventV1 =
        TaskCompletedEventV1(
            id = id,
            assignee = assignee.id,
            description = description,
            created = created,
            priceAmount = priceAmount,
        )
}
