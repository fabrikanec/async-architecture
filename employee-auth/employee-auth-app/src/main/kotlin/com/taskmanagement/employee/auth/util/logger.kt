package com.taskmanagement.employee.auth.util

import org.slf4j.lazyLogger

fun Any.logger() = lazyLogger(this.javaClass)
