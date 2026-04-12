package com.example.github.users.feature.favorites.presentation.favorites

import com.example.github.users.core.ui.component.UserListItem
import com.example.github.users.core.ui.model.UiText

internal sealed interface FavoritesUiState {
    data object Idle : FavoritesUiState
    data class ConfirmRemoval(val item: UserListItem) : FavoritesUiState
    data class LoadError(val message: UiText) : FavoritesUiState
    data class RemovalError(val message: UiText) : FavoritesUiState
}
