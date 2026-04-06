package com.tyme.github.users.core.common.models

data class UserModel(
    val id: Int = 0,
    val username: String = "",
    val avatarUrl: String = "",
    val url: String = "",
    val isFavorite: Boolean = false,
)
