package com.tyme.github.users.feature.favorites.presentation.favorites

import com.tyme.github.users.core.common.models.errors.UiText
import com.tyme.github.users.core.ui.components.UserListItem

sealed interface FavoritesUiState {
    data object Idle : FavoritesUiState
    data class ConfirmRemoval(val item: UserListItem) : FavoritesUiState
    data class RemovalError(val message: UiText) : FavoritesUiState
}
