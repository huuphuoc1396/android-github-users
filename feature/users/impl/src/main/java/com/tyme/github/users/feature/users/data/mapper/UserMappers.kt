package com.tyme.github.users.feature.users.data.mapper

import com.tyme.github.users.core.common.extensions.orZero
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.users.data.local.UserEntity
import com.tyme.github.users.feature.users.data.remote.dto.UserResponse

internal fun UserResponse.toUserEntity(): UserEntity = UserEntity(
    id = id.orZero(),
    username = login.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    url = htmlUrl.orEmpty(),
)

internal fun UserEntity.toUserModel() = UserModel(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
    isFavorite = isFavorite,
)
