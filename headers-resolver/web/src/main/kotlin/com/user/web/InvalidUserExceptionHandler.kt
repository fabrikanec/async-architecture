package com.user.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestControllerAdvice
class InvalidUserExceptionHandler {
    @ExceptionHandler
    fun handleUserConstructionException(ex: UserConstructionException): ResponseEntity<UserConstructionError> {
        val status: HttpStatus = HttpStatus.BAD_REQUEST
        return ResponseEntity.status(status)
            .headers { headers: HttpHeaders ->
                headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_PROBLEM_JSON_VALUE
            }
            .body(
                UserConstructionError(
                    status = status.value(),
                    instance = getInstanceUriFromRequest(),
                    invalidHeaders = ex.invalidHeaders,
                )
            )
    }

    companion object {
        private fun getInstanceUriFromRequest(): String = ServletUriComponentsBuilder.fromCurrentRequest()
            .host(null)
            .scheme(null)
            .build()
            .toUriString()
    }
}
