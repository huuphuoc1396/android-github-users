package com.example.github.users.feature.users.presentation.userlist

import androidx.paging.LoadState
import com.example.github.users.core.ui.component.UserListItem

internal sealed interface UserListUiAction {
    data object Refresh : UserListUiAction
    data class RefreshLoadStateChanged(val loadState: LoadState) : UserListUiAction
    data class UserClick(val user: UserListItem) : UserListUiAction
    data class FavoriteClick(val user: UserListItem) : UserListUiAction
    data object ConfirmRemoveFavorite : UserListUiAction
    data object DismissRemoveFavorite : UserListUiAction
    data object DismissError : UserListUiAction
    data class UrlClick(val url: String) : UserListUiAction
}
