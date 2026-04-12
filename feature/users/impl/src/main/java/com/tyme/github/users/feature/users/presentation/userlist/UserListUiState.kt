package com.tyme.github.users.feature.users.presentation.userlist

import com.tyme.github.users.core.ui.models.UiText
import com.tyme.github.users.core.ui.components.UserListItem

internal sealed interface UserListUiState {
    data object Idle : UserListUiState
    data object Loading : UserListUiState
    data class Success(
        val isRefreshing: Boolean = false,
        val pendingRemoval: UserListItem? = null,
    ) : UserListUiState
    data class Error(val message: UiText) : UserListUiState
}
