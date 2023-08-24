package com.taskmanagement.tasktracker.task.shuffle

import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository
import com.taskmanagement.tasktracker.task.event.flow.v1.mapper.TaskFlowEventMapper
import com.taskmanagement.tasktracker.task.event.stream.v1.mapper.TaskStreamEventMapper
import com.taskmanagement.tasktracker.task.jpa.TaskRepository
import com.taskmanagement.tasktracker.task.jpa.TaskStatus
import com.taskmanagement.tasktracker.task.shuffle.jpa.TaskShuffleRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
class TaskShuffleService(
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskShuffleRepository: TaskShuffleRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val taskFlowEventMapper: TaskFlowEventMapper,
    private val taskStreamEventMapper: TaskStreamEventMapper,
    private val clock: Clock,
) {
    @Transactional
    fun shuffle() {
        val shuffle = taskShuffleRepository.findClosest() ?: return
        val tasks = taskRepository.findLimitedByStatusAndUpdatedLessThen(
            status = TaskStatus.ASSIGNED.name,
            date = shuffle.created,
            limit = limit,
        ).onEach { task ->
            task.assignee = employeeRepository.findOneRandomly()
                ?: throw IllegalStateException("No one entity found")
            task.updated = clock.instant()

            with(taskStreamEventMapper) {
                applicationEventPublisher.publishEvent(task.toStreamEventV1())
            }

            with(taskFlowEventMapper) {
                applicationEventPublisher.publishEvent(task.toTaskReshuffledEventV1())
            }
        }
        if (tasks.isEmpty())
            taskShuffleRepository.delete(shuffle)
        else {
            taskRepository.saveAll(tasks)
            taskShuffleRepository.save(shuffle)
        }
    }

    companion object {
        const val limit = 500
    }
}
