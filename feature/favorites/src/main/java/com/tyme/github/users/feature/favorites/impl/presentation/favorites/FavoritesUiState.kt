package com.tyme.github.users.feature.favorites.impl.presentation.favorites

import com.tyme.github.users.feature.users.api.model.UserModel

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(
        val favorites: List<UserModel>,
        val pendingRemoval: UserModel? = null,
    ) : FavoritesUiState
}
