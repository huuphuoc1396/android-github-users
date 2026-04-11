package com.tyme.github.users.feature.favorites.presentation.favorites

import com.tyme.github.users.core.ui.components.UserListItem

sealed interface FavoritesUiAction {
    data class UserClick(val user: UserListItem) : FavoritesUiAction
    data class RemoveFavoriteClick(val user: UserListItem) : FavoritesUiAction
    data object ConfirmRemoveFavorite : FavoritesUiAction
    data object DismissRemoveFavorite : FavoritesUiAction
    data object DismissError : FavoritesUiAction
    data class UrlClick(val url: String) : FavoritesUiAction
}
