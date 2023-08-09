package com.taskmanagement.employee.auth.login.entity

import com.taskmanagement.employee.auth.util.db.PostgreSqlType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.Hibernate
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@TypeDefs(
    TypeDef(name = PostgreSqlType.JSONB_TYPE, typeClass = JsonBinaryType::class)
)
open class LoginData(
    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "id", column = Column(name = "request_id")),
            AttributeOverride(name = "hash", column = Column(name = "request_hash")),
        ]
    )
    open val request: Request,

    @Type(type = PostgreSqlType.JSONB_TYPE)
    open val requestData: RequestData,

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "from", column = Column(name = "valid_from")),
            AttributeOverride(name = "until", column = Column(name = "valid_until")),
        ]
    )
    open val validity: Validity,
) {
    @Id
    @GeneratedValue
    open val id: UUID? = null

    open var spentDateTime: OffsetDateTime? = null

    fun isSpent(): Boolean =
        spentDateTime != null

    private fun isValidBy(dateTime: OffsetDateTime) =
        dateTime >= validity.from && dateTime <= validity.until

    fun isNotValidBy(dateTime: OffsetDateTime) =
        !isValidBy(dateTime)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as LoginData

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName +
            "(id = $id , request = $request , validity = $validity , spentDateTime = $spentDateTime )"
    }
}
