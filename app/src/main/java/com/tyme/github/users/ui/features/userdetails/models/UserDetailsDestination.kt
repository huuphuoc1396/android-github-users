package com.tyme.github.users.ui.features.userdetails.models

import kotlinx.serialization.Serializable

@Serializable
internal data class UserDetailsDestination(
    val username: String,
)