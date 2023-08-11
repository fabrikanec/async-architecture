package com.taskmanagement.tasktracker.task.shuffle.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("task-shuffle-job")
data class TaskShuffleJobProperties(
    val cron: String,
)
