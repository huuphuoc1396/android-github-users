package com.tyme.github.users.feature.users.presentation.userdetails

import com.tyme.github.users.core.common.models.errors.UiText

sealed interface UserDetailUiState {
    object Idle : UserDetailUiState
    object Loading : UserDetailUiState
    data class Success(
        val username: String,
        val avatarUrl: String,
        val country: String,
        val followers: String,
        val following: String,
        val url: String,
        val showRemoveConfirmDialog: Boolean = false,
    ) : UserDetailUiState
    data class Error(val message: UiText) : UserDetailUiState
}
