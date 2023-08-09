package com.taskmanagement.employee.auth.login.hash

interface DataHasher {
    fun hashToString(byteArray: ByteArray): String
}
