package com.taskmanagement.tasktracker.task.shuffle.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.UUID

@ConstructorBinding
@ConfigurationProperties("task-shuffle")
data class TaskShuffleProperties(
    val id: UUID,
)
