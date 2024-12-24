package com.tyme.github.users.domain.models.errors

data class UnauthorizedException(
    override val message: String = "Unauthorized",
) : RuntimeException()