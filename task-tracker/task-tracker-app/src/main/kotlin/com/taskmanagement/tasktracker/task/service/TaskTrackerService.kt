package com.taskmanagement.tasktracker.task.service

import com.taskmanagement.tasktracker.AddTaskRequest
import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository
import com.taskmanagement.tasktracker.task.jpa.Task
import com.taskmanagement.tasktracker.task.jpa.TaskRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskTrackerService(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
) {
    @Transactional
    fun add(addTaskRequest: AddTaskRequest): Task {
        TODO()
    }

    @Transactional
    fun shuffle() {
    }
}
