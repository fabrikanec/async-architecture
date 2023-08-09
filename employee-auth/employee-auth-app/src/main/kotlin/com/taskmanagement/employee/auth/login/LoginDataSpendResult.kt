package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.login.entity.LoginData

sealed interface LoginDataSpendResult {
    class Spent(val loginData: LoginData) : LoginDataSpendResult
    sealed interface Error : LoginDataSpendResult {
        class NotFound(val hash: String) : Error
        class NotValid(val loginData: LoginData) : Error
        class AlreadySpent(val loginData: LoginData) : Error
    }
}
