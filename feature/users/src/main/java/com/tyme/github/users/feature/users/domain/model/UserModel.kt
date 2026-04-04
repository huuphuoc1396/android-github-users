package com.tyme.github.users.feature.users.domain.model

data class UserModel(
    val id: Int = 0,
    val username: String = "",
    val avatarUrl: String = "",
    val url: String = "",
    val isFavorite: Boolean = false,
)
