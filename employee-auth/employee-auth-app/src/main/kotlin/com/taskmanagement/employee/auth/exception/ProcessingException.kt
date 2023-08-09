package com.taskmanagement.employee.auth.exception

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception

open class ProcessingException(
    open val localizedMessage: LocalizedMessage,
    val type: String? = null,
    override val cause: Throwable? = null,
) : OAuth2Exception(localizedMessage.code)
