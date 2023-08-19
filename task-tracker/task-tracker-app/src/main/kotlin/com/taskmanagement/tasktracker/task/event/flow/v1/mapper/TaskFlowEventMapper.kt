package com.taskmanagement.tasktracker.task.event.flow.v1.mapper

import com.taskmanagement.task.event.flow.TaskAddedEventV1
import com.taskmanagement.task.event.flow.TaskCompletedEventV1
import com.taskmanagement.task.event.flow.TaskReshuffledEventV1
import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class TaskFlowEventMapper(
    private val clock: Clock,
) {
    fun Task.toTaskAddedEventV1(): TaskAddedEventV1 =
        TaskAddedEventV1(
            id = id,
            assigneeId = assignee.id,
            created = created,
        )

    fun Task.toTaskReshuffledEventV1(): TaskReshuffledEventV1 =
        TaskReshuffledEventV1(
            id = id,
            assigneeId = assignee.id,
            created = created,
        )

    fun Task.toTaskCompletedEventV1(): TaskCompletedEventV1 =
        TaskCompletedEventV1(
            id = id,
            created = clock.instant(),
        )
}
