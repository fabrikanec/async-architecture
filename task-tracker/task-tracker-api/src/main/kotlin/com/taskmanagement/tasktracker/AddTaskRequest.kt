package com.taskmanagement.tasktracker

import java.util.UUID

data class AddTaskRequest(
    val description: String,
    val assignee: UUID,
)
