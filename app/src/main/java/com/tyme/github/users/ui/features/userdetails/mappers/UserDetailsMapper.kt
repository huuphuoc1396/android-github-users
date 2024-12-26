package com.tyme.github.users.ui.features.userdetails.mappers

import com.tyme.github.users.domain.models.users.UserDetailsModel
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.ui.features.userdetails.models.UserDetailUiState
import com.tyme.github.users.ui.features.userdetails.models.UserDetailsDestination

internal fun UserModel.toUserDetailsDestination() = UserDetailsDestination(
    username = username,
    avatarUrl = avatarUrl,
    url = url,
)

internal fun UserDetailsModel.toUserDetailsUiState() = UserDetailUiState(
    username = username,
    avatarUrl = avatarUrl,
    country = country,
    followers = "$followers+",
    following = "$following+",
    url = url,
)