package com.taskmanagement.employee.auth.controller

import com.taskmanagement.employee.EmployeeRole
import com.taskmanagement.employee.auth.token.EmployeeOAuth2TokenSupport
import com.taskmanagement.employee.service.EmployeeService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

@WebMvcTest(
    controllers = [
        UserController::class
    ]
)
@ContextConfiguration(
    classes = [
        UserControllerTest.ControllerTestConfig::class
    ]
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
internal class UserControllerTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun service() = mockk<EmployeeService>()

        @Bean
        fun tokenSupport() = mockk<EmployeeOAuth2TokenSupport>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var companyOauth2TokenSupport: EmployeeOAuth2TokenSupport

    @Test
    fun `should return 200 when get user`() {
        every { companyOauth2TokenSupport.roles } answers { setOf(EmployeeRole.COMMON_EMPLOYEE) }
        every { companyOauth2TokenSupport.getOrNullUserClaimId() } answers { UUID.fromString("b9470d76-1307-4b77-88bf-3be6f023f957") }

        mockMvc.get("/users/info").andDo { print() }.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(
                    """
                        {
                          "id": "b9470d76-1307-4b77-88bf-3be6f023f957",
                          "roles": [
                            "COMMON_EMPLOYEE"
                          ]
                        }
                    """.trimIndent(),
                    strict = true,
                )
            }
        }
    }
}
