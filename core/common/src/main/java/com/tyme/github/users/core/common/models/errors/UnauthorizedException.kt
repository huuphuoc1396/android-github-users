package com.tyme.github.users.core.common.models.errors

data class UnauthorizedException(
    override val message: String = "Unauthorized",
) : Exception()
