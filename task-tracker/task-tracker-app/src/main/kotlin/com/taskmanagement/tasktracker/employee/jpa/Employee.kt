package com.taskmanagement.tasktracker.employee.jpa

import com.taskmanagement.tasktracker.util.db.PostgreSqlType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.Hibernate
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@TypeDefs(
    TypeDef(name = PostgreSqlType.JSONB_TYPE, typeClass = JsonBinaryType::class)
)
open class Employee protected constructor(
    @Id
    open val id: UUID = UUID.randomUUID(),
    open val created: Instant,
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

    companion object {
        operator fun invoke(
            id: UUID = UUID.randomUUID(),
            created: Instant,
            roles: MutableSet<EmployeeRole>,
            updated: Instant,
        ): Employee =
            Employee(
                id = id,
                created = created,
            ).apply {
                this.roles = roles
                this.updated = updated
            }
    }
}
