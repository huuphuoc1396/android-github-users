package com.example.github.users.core.common.model.error

data class UnauthorizedException(
    override val message: String = "Unauthorized",
) : Exception()
