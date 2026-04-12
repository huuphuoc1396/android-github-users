package com.example.github.users.feature.users.presentation.userdetails

import com.example.github.users.core.ui.model.UiText

internal sealed interface UserDetailUiState {
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
