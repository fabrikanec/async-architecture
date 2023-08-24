package com.taskmanagement.accounting.task.event.flow.v1

import com.taskmanagement.task.event.flow.TaskAddedEventV1
import com.taskmanagement.task.event.flow.TaskCompletedEventV1
import com.taskmanagement.task.event.flow.TaskReshuffledEventV1
import org.springframework.stereotype.Component

@Component
object TaskFlowEventMapper {
    fun TaskAddedEventV1.toEntity(): Payment =
        Payment(
            id = id,
            created = created,
            assigneeId = assigneeId,
        )

    fun TaskReshuffledEventV1.toEntity(): Payment =
        Payment(
            id = id,
            created = created,
            assigneeId = assigneeId,
        )

    fun TaskCompletedEventV1.toEntity(): Payment =
        Payment(
            id = id,
            created = created,
        )
}
