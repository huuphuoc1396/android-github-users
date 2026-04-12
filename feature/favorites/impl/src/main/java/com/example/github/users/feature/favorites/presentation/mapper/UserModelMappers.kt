package com.example.github.users.feature.favorites.presentation.mapper

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.core.ui.component.UserListItem

internal fun UserModel.toUserListItem() = UserListItem(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    url = url,
    isFavorite = isFavorite,
)
