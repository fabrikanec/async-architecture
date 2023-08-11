package com.taskmanagement.tasktracker.task.shuffle.jpa

import com.taskmanagement.tasktracker.task.jpa.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException
import javax.persistence.LockModeType

@Repository
interface TaskShuffleRepository :
    JpaRepository<TaskShuffle, UUID> {

    @Query(
        """
            select * from task_shuffle
            where id = :id
            for update skip locked
        """,
        nativeQuery = true
    )
    fun findWithLockById(id: UUID): TaskShuffle?

    companion object {

        fun TaskShuffleRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")

        fun TaskShuffleRepository.getByIdWithLockOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")

    }
}
