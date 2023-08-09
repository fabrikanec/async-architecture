package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.login.entity.LoginData
import com.taskmanagement.employee.auth.login.entity.Request
import com.taskmanagement.employee.auth.login.entity.RequestData
import com.taskmanagement.employee.auth.login.entity.Validity
import com.taskmanagement.employee.auth.login.hash.DataHasher
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

open class LoginDataService(
    private val loginDataRepository: LoginDataRepository,
    private val dataHasher: DataHasher,
    private val ttl: Duration,
    private val requestText: String,
) {
    fun generate(): Pair<ByteArray, RequestData> {
        val validFrom = OffsetDateTime.now()
        val requestId = UUID.randomUUID()
        val requestData = requestData(
            id = requestId,
            text = requestText,
            dateTime = validFrom,
        )
        val loginData = loginDataRepository.save(
            LoginData(
                request = Request(
                    id = requestId,
                    hash = dataHasher.hashToString(requestData),
                ),
                requestData = RequestData(
                    id = requestId,
                    text = requestText,
                    dateTime = validFrom,
                ),
                validity = Validity(
                    from = validFrom,
                    until = validFrom + ttl,
                )
            )
        )
        return requestData to loginData.requestData
    }

    @Transactional
    open fun spend(
        data: ByteArray,
        dateTime: OffsetDateTime,
    ): LoginDataSpendResult {
        val dataHash = dataHasher.hashToString(data)
        val loginData = loginDataRepository.findByRequestHashForUpdateOrNull(dataHash)
            ?: return LoginDataSpendResult.Error.NotFound(dataHash)
        if (loginData.isNotValidBy(dateTime))
            return LoginDataSpendResult.Error.NotValid(loginData)
        if (loginData.isSpent())
            return LoginDataSpendResult.Error.AlreadySpent(loginData)
        return LoginDataSpendResult.Spent(
            loginDataRepository.save(
                loginData.apply {
                    spentDateTime = dateTime
                }
            )
        )
    }

    companion object {
        private val REQUEST_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        private fun requestData(
            id: UUID,
            text: String,
            dateTime: OffsetDateTime,
        ): ByteArray =
            listOf(id, text, dateTime.format(REQUEST_DATE_TIME_FORMATTER))
                .joinToString(separator = "|")
                .toByteArray()
    }
}
