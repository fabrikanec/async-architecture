package com.user.web

import com.user.User
import com.user.http.headers.UserHttpHeaders
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.UUID

class UserHandlerMethodArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType.isAssignableFrom(User::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): User =
        webRequest.userOrThrow()

    private fun NativeWebRequest.userOrThrow(): User {
        val invalidHeaders: MutableCollection<InvalidHeader> = mutableListOf()
        val id: UUID? = headerOrInvalid(UserHttpHeaders.id, invalidHeaders)?.toUUIDOrNull()
        val roles: Array<String>? = headerValuesOrInvalid(UserHttpHeaders.roles, invalidHeaders)
        if (invalidHeaders.isNotEmpty())
            throw UserConstructionException(invalidHeaders)
        return User(
            id = id!!,
            roles = roles!!.toSet(),
        )
    }

    private fun String.toUUIDOrNull(): UUID? =
        try {
            UUID.fromString(this)
        } catch (ex: IllegalArgumentException) {
            null
        }

    private fun NativeWebRequest.headerOrInvalid(
        header: String,
        invalidHeaders: MutableCollection<InvalidHeader>,
    ): String? =
        getHeader(header) ?: run {
            invalidHeaders += invalidHeader(header)
            null
        }

    private fun NativeWebRequest.headerValuesOrInvalid(
        header: String,
        invalidHeaders: MutableCollection<InvalidHeader>,
    ): Array<String>? =
        getHeaderValues(header) ?: run {
            invalidHeaders += invalidHeader(header)
            null
        }

    private fun NativeWebRequest.invalidHeader(header: String): InvalidHeader =
        InvalidHeader(header, getHeaderValues(header))
}
