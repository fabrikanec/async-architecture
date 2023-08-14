package com.taskmanagement.tasktracker.task.shuffle.job

import com.taskmanagement.tasktracker.task.shuffle.TaskShuffleService
import org.slf4j.info
import org.slf4j.lazyLogger
import org.slf4j.trace
import org.springframework.stereotype.Component

@Component
class ShuffleJob(
    private val taskShuffleService: TaskShuffleService,
) {

    private val log by lazyLogger(this::class)

    fun run() {
        log.info { "Running: ${this::class.simpleName}" }
        taskShuffleService.shuffle()
        log.trace { "Shuffling finished" }
    }
}
