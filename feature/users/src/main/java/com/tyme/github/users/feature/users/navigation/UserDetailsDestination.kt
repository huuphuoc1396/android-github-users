package com.tyme.github.users.feature.users.navigation

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsDestination(
    val username: String = "",
    val avatarUrl: String = "",
    val url: String = "",
)
