package com.tyme.github.users.feature.users.presentation.mappers

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.ui.components.UserListItem

internal fun UserModel.toUserListItem() = UserListItem(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
    isFavorite = isFavorite,
)

internal fun UserListItem.toUserModel() = UserModel(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
    isFavorite = isFavorite,
)
