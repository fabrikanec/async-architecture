package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.login.entity.LoginData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LoginDataRepository : JpaRepository<LoginData, UUID> {
    @Query(
        """
            select *
            from login_data
            where request_hash = :hash
            for update
        """,
        nativeQuery = true
    )
    fun findByRequestHashForUpdateOrNull(hash: String): LoginData?
}
