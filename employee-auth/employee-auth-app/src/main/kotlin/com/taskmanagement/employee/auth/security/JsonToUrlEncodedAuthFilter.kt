package com.taskmanagement.employee.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.web.savedrequest.Enumerator
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Enumeration
import java.util.stream.Collectors.toMap
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse
import kotlin.collections.Map.Entry

class JsonToUrlEncodedAuthFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (isOauth2TokenRequest(request)) {
            val json: ByteArray = request.inputStream.readBytes()
            val jsonMap: Map<String, String> = objectMapper.readValue(json)
            val parameters: Map<String, Array<String>> = jsonMap.entries.stream()
                .collect(toMap(Entry<String, String>::key) { e -> arrayOf(e.value) })
            val requestWrapper: HttpServletRequest = RequestWrapper(request, parameters)
            filterChain.doFilter(requestWrapper, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private fun isOauth2TokenRequest(request: HttpServletRequest): Boolean =
        request.servletPath == OAUTH_TOKEN_ENDPOINT_URL &&
            request.contentType == APPLICATION_JSON_CONTENT_TYPE

    private class RequestWrapper(
        request: HttpServletRequest?,
        private val params: Map<String, Array<String>>,
    ) : HttpServletRequestWrapper(request) {
        override fun getParameter(name: String): String =
            params[name]?.get(0) ?: ""

        override fun getParameterMap(): Map<String, Array<String>> =
            params

        override fun getParameterNames(): Enumeration<String> =
            Enumerator(params.keys)

        override fun getParameterValues(name: String): Array<String> =
            params.getValue(name)
    }

    companion object {
        private const val APPLICATION_JSON_CONTENT_TYPE = "application/json"
        private const val OAUTH_TOKEN_ENDPOINT_URL = "/oauth/token"
    }
}
