package com.user.web

class UserConstructionException(
    val invalidHeaders: Collection<InvalidHeader>,
) : RuntimeException("Invalid headers ${invalidHeaders.joinToString()}") {
    constructor(vararg invalidHeaders: InvalidHeader) : this(invalidHeaders.toList())
}
