package com.taskmanagement.employee.auth.login.hash

import org.apache.commons.codec.digest.DigestUtils

object Sha512DataHasher : DataHasher {
    override fun hashToString(byteArray: ByteArray): String =
        DigestUtils.sha512Hex(byteArray)
}
