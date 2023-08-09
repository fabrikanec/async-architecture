package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.EntityAutoConfiguration
import com.taskmanagement.employee.auth.login.entity.RequestData
import com.taskmanagement.employee.auth.login.hash.Sha512DataHasher
import org.apache.commons.codec.digest.DigestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@ActiveProfiles("test")
@DataJpaTest(
    properties = [
        "employee-auth-service.login.data.ttl=${LoginDataServiceTest.TTL_IN_SECONDS}S",
        "employee-auth-service.login.data.request.text=${LoginDataServiceTest.REQUEST_TEXT}",
    ]
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [
        EntityAutoConfiguration::class,
        LoginConfig::class,
        Sha512DataHasher::class,
    ]
)
class LoginDataServiceTest {

    @Autowired
    private lateinit var loginDataService: LoginDataService

    @Test
    fun `should generate login data`() {
        val (byteArrayData, requestData) = generate()
        assertThat(byteArrayData).isEqualTo(requestData.byteArrayData())
        assertThat(requestData.text).isEqualTo(REQUEST_TEXT)
        assertThat(requestData.dateTime).isEqualTo(now)
    }

    @Test
    fun `should spend login data`() {
        val (byteArrayData, _) = generate()
        val spentDateTime = now + TTL
        val spendResult = loginDataService.spend(byteArrayData, spentDateTime)
        assertThat(spendResult).isInstanceOf(LoginDataSpendResult.Spent::class.java)
        spendResult as LoginDataSpendResult.Spent
        assertThat(spendResult.loginData.request.hash).isEqualTo(byteArrayData.hash())
        assertThat(spendResult.loginData.spentDateTime).isEqualTo(spentDateTime)
    }

    @Test
    fun `should not spent not existing login data`() {
        val data = "h1k7".toByteArray()
        val spendResult = loginDataService.spend(data, now)
        assertThat(spendResult).isInstanceOf(LoginDataSpendResult.Error.NotFound::class.java)
        spendResult as LoginDataSpendResult.Error.NotFound
        assertThat(spendResult.hash).isEqualTo(data.hash())
    }

    @Test
    fun `should not spend already spent login data`() {
        val (byteArrayData, _) = generate()
        val firstSpendResult = loginDataService.spend(byteArrayData, now)
        assertThat(firstSpendResult).isInstanceOf(LoginDataSpendResult.Spent::class.java)
        firstSpendResult as LoginDataSpendResult.Spent
        val secondSpendResult = loginDataService.spend(byteArrayData, now)
        assertThat(secondSpendResult).isInstanceOf(LoginDataSpendResult.Error.AlreadySpent::class.java)
        secondSpendResult as LoginDataSpendResult.Error.AlreadySpent
        assertThat(firstSpendResult.loginData.id).isEqualTo(secondSpendResult.loginData.id)
    }

    @Test
    fun `should not spend expired login request`() {
        val (byteArrayData, _) = generate()
        val spendResult = loginDataService.spend(byteArrayData, now + TTL + Duration.ofNanos(1))
        assertThat(spendResult).isInstanceOf(LoginDataSpendResult.Error.NotValid::class.java)
        spendResult as LoginDataSpendResult.Error.NotValid
        assertThat(spendResult.loginData.request.hash).isEqualTo(byteArrayData.hash())
    }

    private fun generate(): Pair<ByteArray, RequestData> {
        return loginDataService.generate()
    }

    private fun RequestData.byteArrayData(): ByteArray =
        listOf(id, text, dateTime.format(REQUEST_DATE_TIME_FORMATTER))
            .joinToString(separator = "|")
            .toByteArray()

    private fun ByteArray.hash(): String =
        DigestUtils.sha512Hex(this)

    companion object {
        const val TTL_IN_SECONDS = 57L
        const val REQUEST_TEXT = "Login request"
        private val REQUEST_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        private val TTL = Duration.ofSeconds(TTL_IN_SECONDS)

        private val now = OffsetDateTime.of(
            LocalDate.of(2022, 2, 2),
            LocalTime.of(10, 31, 20),
            ZoneOffset.UTC,
        )
    }
}
