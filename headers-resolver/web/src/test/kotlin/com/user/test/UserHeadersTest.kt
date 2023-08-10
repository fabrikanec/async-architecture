package com.user.test

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [TestController::class])
@AutoConfigureMockMvc(addFilters = false)
class UserHeadersTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return error on invalid user headers`() {
        mockMvc
            .get("/userinfo") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }
            .andDo { print() }
            .andExpect {
                status { isEqualTo(HttpStatus.BAD_REQUEST.value()) }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    json(
                        """
                        {
                          "status": ${HttpStatus.BAD_REQUEST.value()},
                          "title": null,
                          "detail": null,
                          "instance": "/userinfo",
                          "type": "/invalid-user-headers",
                          "invalidHeaders": [
                            {
                              "name": "user-id",
                              "values": null
                            },
                            {
                              "name": "user-roles",
                              "values": null
                            }
                          ]
                        }
                        """.trimIndent(),
                        strict = true,
                    )
                }
            }
    }

    @Test
    fun `should return user on valid user headers`() {
        val userId = "571fea4b-19fd-487e-b639-3eb22da58b36"
        val userRoles = listOf("EMPLOYEE_COMMON", "ACCOUNTER")
        mockMvc
            .get("/userinfo") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                headers {
                    add("user-id", userId)
                    addAll("user-roles", userRoles)
                }
            }
            .andDo { print() }
            .andExpect {
                status { isEqualTo(HttpStatus.OK.value()) }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(
                        """
                        {
                          "id": "$userId",
                          "roles": ${userRoles.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }}
                        }
                        """.trimIndent(),
                        strict = true,
                    )
                }
            }
    }
}
