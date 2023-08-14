package com.taskmanagement.tasktracker.task.event.flow.v1.mapper

import com.taskmanagement.task.event.flow.TaskAssignedEventV1
import com.taskmanagement.task.event.flow.TaskCompletedEventV1
import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.time.Clock

@Component
class TaskFlowEventMapper(
    private val clock: Clock,
) {
    fun Task.toTaskAssignedEventV1(priceAmount: BigInteger): TaskAssignedEventV1 =
        TaskAssignedEventV1(
            id = id,
            assignee = assignee.id,
            created = created,
            priceAmount = priceAmount,
        )

    fun Task.toTaskCompletedEventV1(priceAmount: BigInteger): TaskCompletedEventV1 =
        TaskCompletedEventV1(
            id = id,
            created = clock.instant(),
            priceAmount = priceAmount,
        )
}
