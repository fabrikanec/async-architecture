package com.taskmanagement.employee.auth.controller

import com.taskmanagement.employee.auth.token.EmployeeOAuth2TokenSupport
import com.taskmanagement.employee.auth.util.employee
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

        @Bean
        fun employeeRepository() = mockk<EmployeeRepository>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var employeeOauth2TokenSupport: EmployeeOAuth2TokenSupport

    @Autowired
    private lateinit var employeeService: EmployeeService

    @Test
    fun `should return 200 when get user`() {
        val employee = employee()
        every { employeeService.getOne(any()) } answers { employee }
        every { employeeOauth2TokenSupport.roles } answers { setOf(EmployeeRole.COMMON_EMPLOYEE) }
        every { employeeOauth2TokenSupport.getUserClaimId() } answers { employee.id }

        mockMvc.get("/users/info").andDo { print() }.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(
                    """
                        {
                          "id": ${employee.id},
                          "roles": ${employee.roles}
                        }
                    """.trimIndent(),
                    strict = true,
                )
            }
        }
    }
}
