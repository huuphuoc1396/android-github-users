package com.tyme.github.users.data.repositories.users.mappers

import com.tyme.github.users.data.remote.responses.users.UserResponse
import com.tyme.github.users.domain.extensions.orZero
import com.tyme.github.users.domain.models.users.UserModel

internal fun UserResponse.toUserModel(): UserModel = UserModel(
    id = id.orZero(),
    username = login.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    url = htmlUrl.orEmpty(),
)