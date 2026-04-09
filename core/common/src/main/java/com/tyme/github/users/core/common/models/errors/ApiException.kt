package com.tyme.github.users.core.common.models.errors

data class ApiException(
    val code: Int = 0,
    override val message: String = "",
) : Exception()
