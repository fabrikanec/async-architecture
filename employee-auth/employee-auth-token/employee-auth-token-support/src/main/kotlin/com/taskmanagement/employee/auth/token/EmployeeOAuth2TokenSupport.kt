package com.taskmanagement.employee.auth.token

import java.util.UUID

interface EmployeeOAuth2TokenSupport {
    val roles: Set<EmployeeRole>
    fun hasRole(roles: String): Boolean
    fun hasAnyRole(vararg roles: String): Boolean
    fun getUserClaimId(): UUID
}
