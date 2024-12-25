package com.tyme.github.users.data.repositories.users.mappers

import com.tyme.github.users.data.remote.responses.users.UserResponse
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import com.tyme.github.users.domain.extensions.orZero
import com.tyme.github.users.domain.models.users.UserModel

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
)