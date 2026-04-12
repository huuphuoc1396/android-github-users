package com.example.github.users.core.common.model.error

data class NoConnectionException(
    override val message: String = "No connection",
) : Exception()
