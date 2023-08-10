package com.user.test

import com.user.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("userinfo")
    fun userinfo(user: User): User =
        user
}
