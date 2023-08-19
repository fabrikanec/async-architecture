package com.taskmanagement.tasktracker.task.shuffle.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
interface TaskShuffleRepository :
    JpaRepository<TaskShuffle, UUID> {

    @Query(
        """
            select * from task_shuffle
            order by created desc 
            limit 1
        """,
        nativeQuery = true
    )
    fun findLatest(): TaskShuffle?

    @Query(
        """
            select * from task_shuffle
            order by created asc 
            limit 1
        """,
        nativeQuery = true
    )
    fun findClosest(): TaskShuffle?

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
            findByIdOrNull(id) ?: throw EntityNotFoundException("TaskShuffle with id = [$id] not found")
    }
}
