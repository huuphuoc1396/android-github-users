package com.tyme.github.users.feature.favorites.data.mapper

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.database.entity.UserEntity

internal fun UserEntity.toUserModel() = UserModel(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
    isFavorite = isFavorite,
)
