package com.example.github.users.feature.users.presentation.userdetails

internal sealed interface UserDetailsUiAction {
    data object NavigateBack : UserDetailsUiAction
    data object FavoriteToggle : UserDetailsUiAction
    data object ConfirmRemoveFavorite : UserDetailsUiAction
    data object DismissRemoveFavorite : UserDetailsUiAction
    data object DismissError : UserDetailsUiAction
    data class BlogClick(val url: String) : UserDetailsUiAction
}
