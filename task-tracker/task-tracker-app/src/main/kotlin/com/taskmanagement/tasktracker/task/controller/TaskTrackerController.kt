package com.taskmanagement.tasktracker.task.controller

import com.taskmanagement.tasktracker.AddTaskRequest
import com.taskmanagement.tasktracker.task.service.TaskTrackerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskTrackerController(
    private val taskTrackerService: TaskTrackerService,
) {
    @PostMapping
    fun add(
        @RequestBody addTaskRequest: AddTaskRequest,
    ) = taskTrackerService.add(addTaskRequest)

    @PostMapping("/shuffle")
    fun shuffle() =
        taskTrackerService.shuffle()
}
