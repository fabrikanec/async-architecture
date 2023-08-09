package com.taskmanagement.employee.auth.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.oauth2.common.util.JsonParser

class ObjectMapperJsonParser(
    private val objectMapper: ObjectMapper,
) : JsonParser {
    override fun parseMap(json: String): MutableMap<String, Any> =
        try {
            objectMapper.readValue(json)
        } catch (e: Exception) {
            throw IllegalArgumentException("Cannot parse json", e)
        }

    override fun formatMap(map: MutableMap<String, *>?): String =
        try {
            objectMapper.writeValueAsString(map)
        } catch (e: Exception) {
            throw IllegalArgumentException("Cannot format json", e)
        }
}
