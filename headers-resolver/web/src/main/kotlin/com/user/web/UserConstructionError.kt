package com.user.web

data class UserConstructionError(
    val status: Int,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null,
    val invalidHeaders: Collection<InvalidHeader>?,
) {
    @Suppress("unused")
    val type = TYPE

    companion object {
        private const val TYPE = "/invalid-user-headers"
    }
}
