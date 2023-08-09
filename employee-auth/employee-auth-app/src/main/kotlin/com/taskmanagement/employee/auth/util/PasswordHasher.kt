package com.taskmanagement.employee.auth.util

import java.security.spec.KeySpec
import java.util.HexFormat
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATIONS = 120_000
private const val KEY_LENGTH = 256
private const val SECRET = "SomeRandomSecret"

fun generateHash(password: String): String {
    val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
    val spec: KeySpec = PBEKeySpec(password.toCharArray(), SECRET.toByteArray(), ITERATIONS, KEY_LENGTH)
    val key: SecretKey = factory.generateSecret(spec)
    val hash: ByteArray = key.encoded
    return hash.toHexString()
}

private fun ByteArray.toHexString(): String =
    HexFormat.of().formatHex(this)
