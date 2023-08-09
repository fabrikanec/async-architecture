package com.taskmanagement.employee.auth.jwt

import org.slf4j.lazyLogger
import org.springframework.core.io.AbstractResource
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileUrlResource
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.security.KeyPair

class KeyPairProvider(
    private val clientKeyStore: ClientKeyStore
) {
    class ClientKeyStore(
        val path: String?,
        val password: String,
        val alias: String,
    )

    private val log by lazyLogger(KeyPairProvider::class)

    fun getKeyPair(): KeyPair {
        return KeyStoreKeyFactory(
            getKeyStoreResource(),
            clientKeyStore.password.toCharArray()
        ).getKeyPair(clientKeyStore.alias)
    }

    private fun getKeyStoreResource(): AbstractResource {
        val path = clientKeyStore.path
        return if (path.isNullOrBlank()) {
            log.info("Keystore path is empty. Using embedded $EMBEDDED_JWT_KEYSTORE_PATH")
            ClassPathResource(EMBEDDED_JWT_KEYSTORE_PATH)
        } else {
            log.info("Get key pair by path: $path")
            FileUrlResource(path)
        }
    }

    companion object {
        const val EMBEDDED_JWT_KEYSTORE_PATH = "jwt.jks"
    }
}
