package com.taskmanagement.tasktracker.task.event.flow.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("task-flow")
@ConstructorBinding
data class TaskFlowEventProperties(
    val topic: String,
)
