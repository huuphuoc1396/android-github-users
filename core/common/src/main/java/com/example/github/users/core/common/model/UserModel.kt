package com.example.github.users.core.common.model

data class UserModel(
    val id: Int = 0,
    val username: String = "",
    val avatarUrl: String = "",
    val url: String = "",
    val isFavorite: Boolean = false,
)
