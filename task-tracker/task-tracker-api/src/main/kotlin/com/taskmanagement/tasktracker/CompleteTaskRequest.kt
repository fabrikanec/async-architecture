package com.taskmanagement.tasktracker

import java.util.UUID

data class CompleteTaskRequest(
    val taskId: UUID,
)
