package com.tyme.github.users.feature.favorites.presentation.favorites

import com.tyme.github.users.core.ui.components.UserListItem

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(
        val favorites: List<UserListItem>,
        val pendingRemoval: UserListItem? = null,
    ) : FavoritesUiState
}
