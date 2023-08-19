package com.taskmanagement.task.event.flow

import java.time.Instant
import java.util.UUID

data class TaskReshuffledEventV1(
    val id: UUID,
    val assigneeId: UUID,
    val created: Instant,
) : TaskFlowEvent