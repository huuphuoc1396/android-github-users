package com.tyme.github.users.ui.features.users.models

import com.tyme.github.users.domain.models.users.UserModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class UserListUiState(
    val userList: ImmutableList<UserModel> = persistentListOf(),
)