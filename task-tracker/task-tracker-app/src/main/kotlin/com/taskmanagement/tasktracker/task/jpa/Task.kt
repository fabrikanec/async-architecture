package com.taskmanagement.tasktracker.task.jpa

import com.taskmanagement.tasktracker.employee.jpa.Employee
import com.taskmanagement.tasktracker.util.db.PostgreSqlType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.Hibernate
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@TypeDefs(
    TypeDef(name = PostgreSqlType.JSONB_TYPE, typeClass = JsonBinaryType::class)
)
open class Task(
    @Id
    open val id: UUID = UUID.randomUUID(),
    open val created: Instant,
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    open var assignee: Employee,
    open val description: String,
) {

    @Enumerated(EnumType.STRING)
    open var status: TaskStatus = TaskStatus.ASSIGNED

    open var updated: Instant = created

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Task
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        this::class.simpleName + "(id = $id)"
}
