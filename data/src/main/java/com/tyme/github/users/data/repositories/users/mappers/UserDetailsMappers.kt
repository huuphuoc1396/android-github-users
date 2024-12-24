package com.tyme.github.users.data.repositories.users.mappers

import com.tyme.github.users.data.remote.responses.users.UserDetailsResponse
import com.tyme.github.users.domain.extensions.orZero
import com.tyme.github.users.domain.models.users.UserDetailsModel

internal fun UserDetailsResponse.toUserDetailsModel() = UserDetailsModel(
    username = login.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    country = location.orEmpty(),
    followers = followers.orZero(),
    following = following.orZero(),
    url = htmlUrl.orEmpty(),
)