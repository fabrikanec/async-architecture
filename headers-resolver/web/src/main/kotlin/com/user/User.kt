package com.user

import java.util.UUID

data class User(
    val id: UUID,
    val roles: Collection<String>,
)
