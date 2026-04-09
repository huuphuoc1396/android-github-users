package com.tyme.github.users.core.common.models.errors

data class NoConnectionException(
    override val message: String = "No connection",
) : Exception()
