package com.taskmanagement.employee.auth.exception

import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

class Oauth2ResponseExceptionTranslator(
    private val messageSource: MessageSource,
) : DefaultWebResponseExceptionTranslator() {
    override fun translate(e: Exception): ResponseEntity<OAuth2Exception> {
        val responseEntity = super.translate(e)
        return when (e) {
            is ProcessingException -> buildResponseEntity(responseEntity, e)
            else -> responseEntity
        }
    }

    private fun buildResponseEntity(
        responseEntity: ResponseEntity<OAuth2Exception>,
        ex: ProcessingException,
    ): ResponseEntity<OAuth2Exception> =
        buildResponseEntity(
            responseEntity = responseEntity,
            ex = ex,
            title = messageSource.getMessage(ex.toMessageSourceResolvable(), LocaleContextHolder.getLocale()),
            type = ex.type,
        )

    private fun buildResponseEntity(
        responseEntity: ResponseEntity<OAuth2Exception>,
        ex: OAuth2Exception,
        title: String,
        type: String?,
    ) =
        ResponseEntity(
            checkNotNull(responseEntity.body) { "Body of responseEntity is null" }.apply {
                addAdditionalInformation("title", title)
                addAdditionalInformation("type", type)
                addAdditionalInformation("status", httpErrorCode.toString())
                addAdditionalInformation("instance", getInstanceUriFromRequest())
                addAdditionalInformation("meta", null)
                addAdditionalInformation("detail", ex.cause?.message)
            },
            HttpHeaders().apply {
                setAll(responseEntity.headers.toSingleValueMap())
            },
            responseEntity.statusCode
        )

    companion object {
        private fun getInstanceUriFromRequest(): String = ServletUriComponentsBuilder.fromCurrentRequest()
            .host(null)
            .scheme(null)
            .build()
            .toUriString()

        private fun ProcessingException.toMessageSourceResolvable(): MessageSourceResolvable =
            object : MessageSourceResolvable {
                override fun getCodes(): Array<String> =
                    arrayOf(localizedMessage.code)

                override fun getArguments(): Array<Any?> =
                    localizedMessage.argOrder
                        .associateWith { argName -> localizedMessage.args[argName] }
                        .values
                        .toTypedArray()
            }
    }
}
