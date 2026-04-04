package com.tyme.github.users.feature.favorites.impl.data.mapper

import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.impl.data.local.FavoriteEntity

internal fun FavoriteEntity.toFavoriteUser() = FavoriteUser(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
)

internal fun FavoriteUser.toEntity() = FavoriteEntity(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
)
