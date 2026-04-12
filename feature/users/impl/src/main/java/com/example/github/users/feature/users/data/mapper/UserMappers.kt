package com.example.github.users.feature.users.data.mapper

import com.example.github.users.core.common.extensions.orZero
import com.example.github.users.core.common.model.UserModel
import com.example.github.users.core.database.entity.UserEntity
import com.example.github.users.feature.users.data.remote.dto.UserResponse

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
