package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.login.entity.RequestData
import org.springframework.util.Base64Utils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController(
    private val loginDataService: LoginDataService,
) {
    @GetMapping("/data")
    fun loginData(): LoginRequest {
        val (byteArrayData, data) = loginDataService.generate()
        return LoginRequest(
            base64Data = Base64Utils.encodeToString(byteArrayData),
            info = data.toLoginRequestInfo(),
        )
    }

    companion object {
        fun RequestData.toLoginRequestInfo(): LoginRequest.Info =
            LoginRequest.Info(
                id = id,
                text = text,
                dateTime = dateTime
            )
    }
}
