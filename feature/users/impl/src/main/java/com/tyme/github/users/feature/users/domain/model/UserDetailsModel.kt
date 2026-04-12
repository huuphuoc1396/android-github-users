package com.tyme.github.users.feature.users.domain.model

internal data class UserDetailsModel(
    val username: String,
    val avatarUrl: String,
    val country: String,
    val followers: Int,
    val following: Int,
    val url: String,
)
