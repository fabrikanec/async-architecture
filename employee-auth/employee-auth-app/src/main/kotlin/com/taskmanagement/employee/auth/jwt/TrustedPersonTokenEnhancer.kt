package com.taskmanagement.employee.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.taskmanagement.employee.auth.jwt.JwtTokenGranter.Companion.PASSWORD_GRANT_TYPE
import com.taskmanagement.employee.auth.token.Paths
import com.taskmanagement.employee.auth.token.UserClaim
import com.taskmanagement.employee.service.EmployeeService
import org.slf4j.info
import org.slf4j.lazyLogger
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer

class TrustedPersonTokenEnhancer(
    private val employeeService: EmployeeService,
    private val objectMapper: ObjectMapper,
) : TokenEnhancer {
    private val log by lazyLogger(TrustedPersonTokenEnhancer::class)

    override fun enhance(
        accessToken: OAuth2AccessToken,
        authentication: OAuth2Authentication,
    ): OAuth2AccessToken {

        val employeePrincipal = when {
            authentication.oAuth2Request.grantType == PASSWORD_GRANT_TYPE -> {
                authentication.principal as? EmployeePrincipal ?: error("Cannot determine user")
            }

            authentication.oAuth2Request.refreshTokenRequest != null -> {
                trustedPersonPrincipalFromRefreshToken(accessToken)
            }

            else -> null
        }

        return if (employeePrincipal != null) {
            log.info { "Enhancing access token with details. Authentication object - $authentication" }
            (accessToken as DefaultOAuth2AccessToken).apply {
                additionalInformation = buildAdditionalInfoMap(employeePrincipal)
            }
        } else {
            log.info { "Return access token without enhancements. Authentication object - $authentication" }
            accessToken
        }
    }

    private fun trustedPersonPrincipalFromRefreshToken(accessToken: OAuth2AccessToken): EmployeePrincipal {
        val decodedRefreshToken = JwtHelper.decode(accessToken.refreshToken.value)
        val refreshTokenMap: Map<String, Any?> = objectMapper.readValue(decodedRefreshToken.claims)
        fun fieldOrThrow(name: String) =
            refreshTokenMap[name] ?: error("Field not found: $name")

        val user = objectMapper.convertValue<UserClaim>(fieldOrThrow(Paths.USER))
        throw RuntimeException("unable to parse refresh token")
//        return with(user) {
//            EmployeePrincipal(
//                id = id,
//                roles = roles,
//            )
//        }
    }

    private fun buildAdditionalInfoMap(principal: EmployeePrincipal): Map<String, Any> {
        return mapOf(
            Paths.USER to UserClaim(
                id = principal.id,
                roles = principal.roles,
            )
        )
    }
}
