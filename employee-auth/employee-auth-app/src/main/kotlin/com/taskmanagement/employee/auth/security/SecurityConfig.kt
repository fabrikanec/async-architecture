package com.taskmanagement.employee.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.Filter

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@Order(SecurityProperties.BASIC_AUTH_ORDER)
class SecurityConfig(
    private val objectMapper: ObjectMapper,
) : WebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder() =
        BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider() =
        DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder())
            setUserDetailsService(userDetailsService())
        }

    @Bean
    fun jsonToUrlEncodedAuthFilter(): Filter =
        JsonToUrlEncodedAuthFilter(
            objectMapper,
        )

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .authenticationProvider(authenticationProvider())
            .userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
            .addFilterAfter(jsonToUrlEncodedAuthFilter(), BasicAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .anyRequest().authenticated()
            .and().httpBasic().and()
            .csrf().disable()
    }
}
