package com.tyme.github.users.feature.users.presentation.userdetails

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
    ) : UserDetailUiState
    data class Error(val message: String) : UserDetailUiState
}
