package com.taskmanagement.tasktracker.task.shuffle.jpa

import org.hibernate.Hibernate
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class TaskShuffle(
    @Id
    open var created: Instant,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as TaskShuffle
        return created == other.created
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        this::class.simpleName + "(created = $created)"
}
