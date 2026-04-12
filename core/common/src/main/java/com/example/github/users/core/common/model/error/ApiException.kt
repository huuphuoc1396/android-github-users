package com.example.github.users.core.common.model.error

data class ApiException(
    val code: Int = 0,
    override val message: String = "",
) : Exception()
