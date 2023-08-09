package com.taskmanagement.employee.jpa

import com.taskmanagement.employee.EmployeeRole
import com.taskmanagement.employee.auth.util.db.PostgreSqlType
import org.hibernate.Hibernate
import org.hibernate.annotations.Type
import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class Employee(
    @Id
    open val id: UUID = UUID.randomUUID(),
    open val created: Instant,
    open val login: String,
    open val password: String,
) {

    open var updated: Instant = created

    @Type(type = PostgreSqlType.JSONB_TYPE)
    open var roles: MutableSet<EmployeeRole> = mutableSetOf(EmployeeRole.COMMON_EMPLOYEE)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Employee
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String =
        this::class.simpleName + "(id = $id)"
}