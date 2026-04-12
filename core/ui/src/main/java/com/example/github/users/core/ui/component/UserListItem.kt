package com.example.github.users.core.ui.component

data class UserListItem(
    val id: Int = 0,
    val username: String = "",
    val avatarUrl: String = "",
    val url: String = "",
    val isFavorite: Boolean = false,
)
