package com.taskmanagement.tasktracker.task.event.stream.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("task-stream")
@ConstructorBinding
data class TaskStreamEventProperties(
    val topic: String,
)
