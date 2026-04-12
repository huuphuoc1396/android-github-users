package com.example.github.users.feature.users.presentation.mapper

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.core.ui.component.UserListItem

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
