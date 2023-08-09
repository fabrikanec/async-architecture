package com.taskmanagement.employee.auth.exception

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.Locale

@Configuration(proxyBeanMethods = false)
class OAuthExceptionTranslatorConfig {

    @Bean
    fun localeResolver(): LocaleResolver =
        AcceptHeaderLocaleResolver().apply {
            defaultLocale = RU_RU_LOCALE
        }

    @Bean
    fun messageSource(): MessageSource =
        ResourceBundleMessageSource().apply {
            setBasenames("i18n/title")
            setDefaultEncoding(Charsets.UTF_8.name())
            setUseCodeAsDefaultMessage(true)
        }

    @Bean
    fun oauth2ResponseExceptionTranslator(
        messageSource: MessageSource,
    ): WebResponseExceptionTranslator<OAuth2Exception> =
        Oauth2ResponseExceptionTranslator(
            messageSource,
        )

    companion object {
        private val RU_RU_LOCALE = Locale("ru", "RU")
    }
}
