package com.taskmanagement.tasktracker.task.event.flow.v1.mapper

import com.taskmanagement.task.event.flow.TaskAssignedEventV1
import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.stereotype.Component

@Component
object TaskFlowEventMapper {
    fun Task.toEvent(): TaskAssignedEventV1 =
        TaskAssignedEventV1(
            id = id,
            assignee = assignee.id,
            description = description,
            created = created,
        )
}
