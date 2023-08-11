package com.taskmanagement.tasktracker.task.event.flow.v1.mapper

import com.taskmanagement.task.event.flow.TaskAssignedEventV1
import com.taskmanagement.task.event.flow.TaskCompletedEventV1
import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.stereotype.Component

@Component
object TaskFlowEventMapper {
    fun Task.toTaskAssignedEventV1(): TaskAssignedEventV1 =
        TaskAssignedEventV1(
            id = id,
            assignee = assignee.id,
            description = description,
            created = created,
        )
    fun Task.toTaskCompletedEventV1(): TaskCompletedEventV1 =
        TaskCompletedEventV1(
            id = id,
            assignee = assignee.id,
            description = description,
            created = created,
        )
}
