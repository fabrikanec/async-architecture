package com.taskmanagement.tasktracker.task.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
interface TaskRepository :
    JpaRepository<Task, UUID> {

    companion object {

        fun TaskRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")
    }
}
