package com.example.github.users.feature.users.data.mapper

import com.example.github.users.core.common.extensions.orZero
import com.example.github.users.feature.users.data.remote.dto.UserDetailsResponse
import com.example.github.users.feature.users.domain.model.UserDetailsModel
import com.example.github.users.feature.users.presentation.userdetails.UserDetailUiState

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
