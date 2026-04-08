package com.tyme.github.users.feature.favorites.presentation.favorites

import androidx.annotation.StringRes
import com.tyme.github.users.core.ui.components.UserListItem

sealed interface FavoritesUiState {
    data object Idle : FavoritesUiState
    data class ConfirmRemoval(val item: UserListItem) : FavoritesUiState
    data class RemovalError(
        val message: String = "",
        @StringRes val messageRes: Int = 0,
    ) : FavoritesUiState
}
