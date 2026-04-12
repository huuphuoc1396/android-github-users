package com.example.github.users.feature.users.presentation.userlist

import com.example.github.users.core.ui.component.UserListItem
import com.example.github.users.core.ui.model.UiText

internal sealed interface UserListUiState {
    data object Idle : UserListUiState
    data object Loading : UserListUiState
    data class Success(
        val isRefreshing: Boolean = false,
        val pendingRemoval: UserListItem? = null,
    ) : UserListUiState
    data class Error(val message: UiText) : UserListUiState
}
