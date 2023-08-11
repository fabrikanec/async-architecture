package com.taskmanagement.tasktracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskTrackerApp

fun main(args: Array<String>) {
    runApplication<TaskTrackerApp>(*args)
}
