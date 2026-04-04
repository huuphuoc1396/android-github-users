package com.tyme.github.users.feature.favorites.impl.presentation.favorites

import com.tyme.github.users.feature.favorites.api.model.FavoriteUser

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(val favorites: List<FavoriteUser>) : FavoritesUiState
}
