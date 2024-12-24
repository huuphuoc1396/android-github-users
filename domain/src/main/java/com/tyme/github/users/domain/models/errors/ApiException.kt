package com.tyme.github.users.domain.models.errors

data class ApiException(
    val code: Int = 0,
    override val message: String = "",
) : RuntimeException()