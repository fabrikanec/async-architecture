package com.taskmanagement.tasktracker.task.service

import com.taskmanagement.employee.EmployeeRole
import com.taskmanagement.tasktracker.AddTaskRequest
import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository
import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import com.taskmanagement.tasktracker.task.event.flow.v1.mapper.TaskFlowEventMapper
import com.taskmanagement.tasktracker.task.jpa.Task
import com.taskmanagement.tasktracker.task.jpa.TaskRepository
import com.taskmanagement.tasktracker.task.jpa.TaskRepository.Companion.getByIdWithLockOrThrow
import com.taskmanagement.tasktracker.task.jpa.TaskStatus
import com.taskmanagement.tasktracker.task.price.PriceResolver
import com.taskmanagement.tasktracker.task.shuffle.config.properties.TaskShuffleProperties
import com.taskmanagement.tasktracker.task.shuffle.jpa.TaskShuffleRepository
import com.user.User
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.UUID

@Service
@EnableConfigurationProperties(TaskShuffleProperties::class)
class TaskTrackerService(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskFlowEventMapper: TaskFlowEventMapper,
    private val taskShuffleRepository: TaskShuffleRepository,
    private val taskShuffleProperties: TaskShuffleProperties,
    private val priceResolver: PriceResolver,
    private val clock: Clock,
) {
    @Transactional
    fun add(addTaskRequest: AddTaskRequest): Task {
        val assignee = employeeRepository.getByIdOrThrow(addTaskRequest.assignee)
        val task = Task(
            created = clock.instant(),
            assignee = assignee,
            description = addTaskRequest.description,
        )
        taskRepository.save(task)
        with(taskFlowEventMapper) {
            applicationEventPublisher.publishEvent(task.toTaskAssignedEventV1(priceAmount = priceResolver.priceToCharge))
        }
        return task
    }

    @Transactional
    fun complete(
        taskId: UUID,
        user: User,
    ) {
        val task = taskRepository.getByIdWithLockOrThrow(taskId)
        val shuffle = taskShuffleRepository.findWithLockById(taskShuffleProperties.id) ?:
        throw IllegalStateException("There is a shuffle in progress")
        require(task.assignee.id == user.id) {
            "You are not assigned to this task"
        }
        require(!shuffle.active) {
            "There is a shuffle in progress"
        }

        task.status = TaskStatus.COMPLETED
        taskRepository.save(task)
        with(taskFlowEventMapper) {
            applicationEventPublisher.publishEvent(task.toTaskCompletedEventV1(priceAmount = priceResolver.priceToPay))
        }
    }

    @Transactional
    fun shuffle(
        user: User,
    ) {
        require(user.roles.any { it in shufflePerimitedRoles }) {
            "You are not allowed to shuffle"
        }
        val shuffle = taskShuffleRepository.findWithLockById(taskShuffleProperties.id)
            ?: throw IllegalStateException("Shuffle already in progress")

        require(shuffle.active == false) {
            "Shuffle already in progress"
        }

        shuffle.active = true
        taskShuffleRepository.save(shuffle)
    }

    @Transactional(readOnly = true)
    fun getAll(
        user: User,
        pageable: Pageable,
    ): Page<Task> =
        taskRepository.findAllByAssigneeId(assigneeId = user.id, pageable = pageable)

    companion object {
        private val shufflePerimitedRoles = setOf(
            EmployeeRole.MANAGER.name,
            EmployeeRole.ADMINISTRATOR.name,
        )
    }
}
