package com.tyme.github.users.feature.favorites.presentation.favorites

import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.core.ui.models.UiText

internal sealed interface FavoritesUiState {
    data object Idle : FavoritesUiState
    data class ConfirmRemoval(val item: UserListItem) : FavoritesUiState
    data class LoadError(val message: UiText) : FavoritesUiState
    data class RemovalError(val message: UiText) : FavoritesUiState
}
