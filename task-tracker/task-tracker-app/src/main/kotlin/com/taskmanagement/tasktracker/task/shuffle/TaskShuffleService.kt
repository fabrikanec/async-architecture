package com.taskmanagement.tasktracker.task.shuffle

import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository
import com.taskmanagement.tasktracker.task.event.flow.v1.mapper.TaskFlowEventMapper
import com.taskmanagement.tasktracker.task.jpa.TaskRepository
import com.taskmanagement.tasktracker.task.jpa.TaskStatus
import com.taskmanagement.tasktracker.task.shuffle.config.properties.TaskShuffleProperties
import com.taskmanagement.tasktracker.task.shuffle.jpa.TaskShuffleRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@Service
@EnableConfigurationProperties(TaskShuffleProperties::class)
class TaskShuffleService(
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskShuffleRepository: TaskShuffleRepository,
    private val taskShuffleProperties: TaskShuffleProperties,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val taskFlowEventMapper: TaskFlowEventMapper,
) {
    @Transactional
    fun shuffle() {
        val shuffleId = taskShuffleProperties.id
        val shuffle = taskShuffleRepository.findWithLockById(shuffleId) ?: return
        if (!shuffle.active) return
        //replace for pagination for better memory usage: findAll for tasks should be replaced with pagination and findAssignee too
        val tasks = taskRepository.findAllByStatus(status = TaskStatus.ASSIGNED.name).onEach {
            it.assignee = employeeRepository.findOneRandomly()
                ?: throw IllegalStateException("No one entity found")
            with(taskFlowEventMapper) {
                applicationEventPublisher.publishEvent(it.toTaskAssignedEventV1())
            }
        }
        taskRepository.saveAll(tasks)

        shuffle.active = false
        taskShuffleRepository.save(shuffle)
    }
}
