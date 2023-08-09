package com.taskmanagement.employee.auth.exception

class ParameterIsNotPresentException(
    val name: String,
) : ProcessingException(
    localizedMessage = DefaultLocalizedMessage("parameter_is_not_present", mapOf(NAME to name), listOf(NAME)),
    type = "/parameter-not-present",
) {
    companion object {
        private const val NAME = "name"
    }
}

object LoginDataNotFoundException : ProcessingException(
    localizedMessage = DefaultLocalizedMessage("login_data_not_found"),
    type = "/login-data/not-found",
)

object LoginDataNotValidException : ProcessingException(
    localizedMessage = DefaultLocalizedMessage("login_data_expired"),
    type = "/login-data/not-valid",
)

object LoginDataAlreadySpentException : ProcessingException(
    localizedMessage = DefaultLocalizedMessage("login_data_already_spent"),
    type = "/login-data/already-spent",
)

class Base64FormatErrorException(
    field: String,
    message: String?,
    cause: Throwable? = null,
) : ProcessingException(
    localizedMessage = DefaultLocalizedMessage(
        "base64_format_error",
        mapOf(FIELD to field, MESSAGE to message),
        listOf(FIELD, MESSAGE)
    ),
    type = "/base64-format-error",
    cause = cause,
) {
    companion object {
        private const val FIELD = "field"
        private const val MESSAGE = "message"
    }
}
