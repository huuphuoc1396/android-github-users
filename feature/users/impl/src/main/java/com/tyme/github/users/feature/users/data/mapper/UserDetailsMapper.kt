package com.tyme.github.users.feature.users.data.mapper

import com.tyme.github.users.domain.extensions.orZero
import com.tyme.github.users.feature.users.data.remote.dto.UserDetailsResponse
import com.tyme.github.users.feature.users.domain.model.UserDetailsModel
import com.tyme.github.users.feature.users.presentation.userdetails.UserDetailUiState

internal fun UserDetailsResponse.toUserDetailsModel() = UserDetailsModel(
    username = login.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    country = location.orEmpty(),
    followers = followers.orZero(),
    following = following.orZero(),
    url = htmlUrl.orEmpty(),
)

internal fun UserDetailsModel.toUserDetailUiState() = UserDetailUiState.Success(
    username = username,
    avatarUrl = avatarUrl,
    country = country,
    followers = "$followers+",
    following = "$following+",
    url = url,
)
