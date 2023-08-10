package com.api.gateway

import com.api.gateway.util.doGet
import com.api.gateway.util.doPost
import com.api.gateway.util.stubForGetRequest
import com.api.gateway.util.stubForPostRequest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class AuthRoutingTest : ApiGateWayAppContextTest() {

    @ParameterizedTest
    @MethodSource("employeeAuthRequestGETUrls")
    fun `should handle GET employee-auth-request ok`(url: String, matchUrl: String) {
        stubForGetRequest(url = matchUrl, data = """{}""")

        client.doGet(url)
    }

    @ParameterizedTest
    @MethodSource("employeeAuthRequestPOSTUrls")
    fun `should handle POST employee-auth-request ok`(url: String, matchUrl: String) {
        stubForPostRequest(url = matchUrl, data = """{}""")

        client.doPost(url)
    }

    companion object {
        @JvmStatic
        fun employeeAuthRequestGETUrls(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "/userinfo",
                "/users/info",
            ),
        )

        @JvmStatic
        fun employeeAuthRequestPOSTUrls(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "/token",
                "/oauth/token",
            ),
        )
    }
}
