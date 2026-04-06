package com.tyme.github.users.feature.users.presentation.userlist

import androidx.annotation.StringRes
import com.tyme.github.users.core.ui.components.UserListItem

sealed interface UserListUiState {
    data object Idle : UserListUiState
    data object Loading : UserListUiState
    data class Success(
        val isRefreshing: Boolean = false,
        val pendingRemoval: UserListItem? = null,
    ) : UserListUiState
    data class Error(val message: String = "", @StringRes val messageRes: Int = 0) : UserListUiState
}
