package com.taskmanagement.employee.auth.jwt

import com.taskmanagement.employee.auth.exception.Base64FormatErrorException
import com.taskmanagement.employee.auth.exception.Checks
import com.taskmanagement.employee.auth.exception.LoginDataAlreadySpentException
import com.taskmanagement.employee.auth.exception.LoginDataNotFoundException
import com.taskmanagement.employee.auth.exception.LoginDataNotValidException
import com.taskmanagement.employee.auth.exception.ParameterIsNotPresentException
import com.taskmanagement.employee.auth.login.LoginDataService
import com.taskmanagement.employee.auth.login.LoginDataSpendResult
import com.taskmanagement.employee.auth.util.generateHash
import com.taskmanagement.employee.service.EmployeeService
import org.slf4j.debug
import org.slf4j.lazyLogger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2RequestFactory
import org.springframework.security.oauth2.provider.TokenRequest
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices
import org.springframework.util.Base64Utils
import java.net.URLDecoder
import java.time.OffsetDateTime

class JwtTokenGranter private constructor(
    tokenServices: AuthorizationServerTokenServices,
    clientDetailsService: ClientDetailsService,
    requestFactory: OAuth2RequestFactory,
    private val employeeService: EmployeeService,
    private val loginDataService: LoginDataService,
) : AbstractTokenGranter(tokenServices, clientDetailsService, requestFactory, PASSWORD_GRANT_TYPE) {
    private val log by lazyLogger(JwtTokenGranter::class)

    constructor(
        endpointsConfigurer: AuthorizationServerEndpointsConfigurer,
        employeeService: EmployeeService,
        loginDataService: LoginDataService,
    ) : this(
        endpointsConfigurer.tokenServices,
        endpointsConfigurer.clientDetailsService,
        endpointsConfigurer.oAuth2RequestFactory,
        employeeService,
        loginDataService,
    )

    override fun getOAuth2Authentication(
        client: ClientDetails,
        tokenRequest: TokenRequest,
    ): OAuth2Authentication =
        OAuth2Authentication(
            tokenRequest.createOAuth2Request(client),
            buildUserAuth(
                LinkedHashMap(tokenRequest.requestParameters)
            )
        )

    private fun buildUserAuth(params: Map<String, String>): UsernamePasswordAuthenticationToken =
        UsernamePasswordAuthenticationToken(
            userPrincipal(
                data = dataFromParamsOrThrow(params),
                username = loginFromParamsOrThrow(params),
                password = passwordFromParamsOrThrow(params),
            ),
            null,
        ).apply {
            details = params
        }

    private fun dataFromParamsOrThrow(params: Map<String, String>): ByteArray {
        val base64Data = Checks.checkNotNull(params[DATA_TOKEN_PARAM]) {
            ParameterIsNotPresentException(DATA_TOKEN_PARAM)
        }.also { base64data ->
            log.debug { "Data before normalization: $base64data" }
        }.decodedUrl().urlBase64ToDefaultBase64().also { base64data ->
            log.debug { "Data after normalization: $base64data" }
        }
        return try {
            Base64Utils.decodeFromString(base64Data)
        } catch (ex: Exception) {
            throw Base64FormatErrorException(DATA_TOKEN_PARAM, ex.message, ex)
        }
    }

    private fun passwordFromParamsOrThrow(params: Map<String, String>): String =
        Checks.checkNotNull(params[PASSWORD_PARAM]) {
            ParameterIsNotPresentException(PASSWORD_PARAM)
        }

    private fun loginFromParamsOrThrow(params: Map<String, String>): String =
        Checks.checkNotNull(params[LOGIN_PARAM]) {
            ParameterIsNotPresentException(LOGIN_PARAM)
        }

    private fun userPrincipal(
        data: ByteArray,
        username: String,
        password: String,
    ): EmployeePrincipal {
        spentLoginDataOrThrow(data, OffsetDateTime.now())

        val base64Password = generateHash(password = password)

        val employee = employeeService.getOneByAuth(username)

        return with(employee) {
            EmployeePrincipal(
                id = id,
                roles = roles,
                username = username,
                password = password,
            )
        }
    }

    companion object {
        const val PASSWORD_GRANT_TYPE = "password"
        const val DATA_TOKEN_PARAM = "data"
        const val PASSWORD_PARAM = "password"
        const val LOGIN_PARAM = "login"

        private fun String.urlBase64ToDefaultBase64(): String =
            this
                .replace("_", "/")
                .replace("-", "+")
                .replace(" ", "+")

        private fun String.decodedUrl(): String =
            URLDecoder.decode(this, Charsets.UTF_8.name())
    }

    private fun spentLoginDataOrThrow(
        data: ByteArray,
        dateTime: OffsetDateTime,
    ) =
        when (val spendResult = loginDataService.spend(data, dateTime)) {
            is LoginDataSpendResult.Spent -> spendResult.loginData
            is LoginDataSpendResult.Error.NotFound -> throw LoginDataNotFoundException
            is LoginDataSpendResult.Error.NotValid -> throw LoginDataNotValidException
            is LoginDataSpendResult.Error.AlreadySpent -> throw LoginDataAlreadySpentException
        }
}
