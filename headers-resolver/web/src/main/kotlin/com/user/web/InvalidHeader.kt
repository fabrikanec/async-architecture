package com.user.web

data class InvalidHeader(val name: String, val values: List<String>?) {
    constructor(name: String, values: Array<String>?) : this(name, values?.toList())
}
