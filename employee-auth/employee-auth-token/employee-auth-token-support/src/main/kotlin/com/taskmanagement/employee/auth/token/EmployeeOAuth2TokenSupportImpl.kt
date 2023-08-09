package com.taskmanagement.employee.auth.token

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.taskmanagement.employee.EmployeeRole
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.security.oauth2.provider.token.TokenStore
import java.util.UUID

class EmployeeOAuth2TokenSupportImpl(
    private val tokenStore: TokenStore,
    private val objectMapper: ObjectMapper,
) : EmployeeOAuth2TokenSupport {
    private val user: UserClaim
        get() = tokenAdditionalInfo(Paths.USER) ?: error("No user in token")

    override val roles: Set<EmployeeRole>
        get() = user.roles

    override fun hasRole(roles: String): Boolean =
        hasAnyRole(roles)

    override fun hasAnyRole(vararg roles: String): Boolean {
        val userRolesStr: Set<String> = this.roles.mapTo(mutableSetOf(), EmployeeRole::name)
        return userRolesStr.any { role: String ->
            role in userRolesStr
        }
    }

    override fun getOrNullUserClaimId(): UUID? =
        user.id

    private fun rawTokenAdditionalInfo(key: String): Any? =
        (SecurityContextHolder.getContext().authentication?.details as? OAuth2AuthenticationDetails)?.let { details ->
            tokenStore.readAccessToken(details.tokenValue)
                .additionalInformation[key]
        }

    private inline fun <reified T : Any> tokenAdditionalInfo(key: String): T? =
        rawTokenAdditionalInfo(key)?.let { objForKey ->
            objectMapper.convertValue(objForKey)
        }
}
