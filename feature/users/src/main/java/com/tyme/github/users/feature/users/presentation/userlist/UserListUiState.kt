package com.tyme.github.users.feature.users.presentation.userlist

sealed interface UserListUiState {
    data object Idle : UserListUiState
    data object Loading : UserListUiState
    data class Success(val isRefreshing: Boolean = false) : UserListUiState
    data class Error(val message: String) : UserListUiState
}
