package com.taskmanagement.task.event.flow

import java.time.Instant
import java.util.UUID

data class TaskCompletedEventV1(
    val id: UUID,
    val assignee: UUID,
    val description: String,
    val created: Instant,
) : TaskFlowEvent
