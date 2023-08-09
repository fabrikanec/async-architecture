package com.taskmanagement.employee.auth.oauth2

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.provider.ClientDetails

@Suppress("TooManyFunctions")
data class EmployeeAuthClientDetails(
    private val clientId: String,
    private val clientSecret: String,
    private val scope: Set<String>,
    private val authorizedGrantTypes: Set<String>,
    private val registeredRedirectUri: Set<String>,
    private val resourceIds: Set<String>,
    private val authorities: Set<String>,
    private val accessTokenValiditySeconds: Int,
    private val refreshTokenValiditySeconds: Int
) : ClientDetails {

    override fun getClientId() = clientId

    override fun getClientSecret() = clientSecret

    override fun isSecretRequired() = true

    override fun getResourceIds() = resourceIds

    override fun getScope() = scope

    override fun isScoped() = scope.isNotEmpty()

    override fun getAuthorizedGrantTypes() = authorizedGrantTypes

    override fun getRegisteredRedirectUri() = registeredRedirectUri

    override fun isAutoApprove(scope: String) = false

    override fun getAuthorities() = authorities.map { SimpleGrantedAuthority(it) }

    override fun getAccessTokenValiditySeconds() = accessTokenValiditySeconds

    override fun getRefreshTokenValiditySeconds() = refreshTokenValiditySeconds

    override fun getAdditionalInformation() = emptyMap<String, Any>()
}
