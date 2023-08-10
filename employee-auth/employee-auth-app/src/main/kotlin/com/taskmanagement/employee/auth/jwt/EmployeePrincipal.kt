package com.taskmanagement.employee.auth.jwt

import com.taskmanagement.employee.EmployeeRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import java.util.UUID

class EmployeePrincipal(
    val id: UUID,
    val roles: Set<EmployeeRole>,
    password: String,
    username: String,
    authorityCollection: Collection<GrantedAuthority> = AuthorityUtils.createAuthorityList("USER", "write"),
) : User(
    username,
    password,
    authorityCollection,
)
