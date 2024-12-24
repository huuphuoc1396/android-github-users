package com.tyme.github.users.domain.models.errors

data class NoConnectionException(
    override val message: String = "No connection",
) : Exception()