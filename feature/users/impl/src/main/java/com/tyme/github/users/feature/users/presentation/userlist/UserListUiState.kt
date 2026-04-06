package com.tyme.github.users.feature.users.presentation.userlist

import androidx.annotation.StringRes
import com.tyme.github.users.core.common.models.UserModel

sealed interface UserListUiState {
    data object Idle : UserListUiState
    data object Loading : UserListUiState
    data class Success(
        val isRefreshing: Boolean = false,
        val pendingRemoval: UserModel? = null,
    ) : UserListUiState
    data class Error(val message: String = "", @StringRes val messageRes: Int = 0) : UserListUiState
}
