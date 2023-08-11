package com.taskmanagement.tasktracker

import java.time.Instant
import java.util.UUID

data class TaskResponseDto(
    val id: UUID,
    val created: Instant,
    val description: String,
)
