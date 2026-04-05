package com.tyme.github.users.feature.favorites.presentation.favorites

import com.tyme.github.users.domain.models.UserModel

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(
        val favorites: List<UserModel>,
        val pendingRemoval: UserModel? = null,
    ) : FavoritesUiState
}
