package com.example.github.users.feature.favorites.data.mapper

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.core.database.entity.UserEntity

internal fun UserEntity.toUserModel() = UserModel(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
    isFavorite = isFavorite,
)
