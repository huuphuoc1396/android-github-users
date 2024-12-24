package com.tyme.github.users.ui.features.userdetails.models

internal data class UserDetailUiState(
    val username: String = "",
    val avatarUrl: String = "",
    val country: String = "",
    val followers: String = "",
    val following: String = "",
    val url: String = "",
)