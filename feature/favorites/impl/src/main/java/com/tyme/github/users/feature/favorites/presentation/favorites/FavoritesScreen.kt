package com.tyme.github.users.feature.favorites.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.core.ui.components.ErrorDialog
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.core.ui.components.RemoveFavoriteDialog
import com.tyme.github.users.core.ui.components.UserList
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.favorites.impl.R
import kotlinx.coroutines.flow.flowOf

@Composable
fun FavoritesScreen(
    onUrlClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites = viewModel.favorites.collectAsLazyPagingItems()
    FavoritesContent(
        favorites = favorites,
        uiState = uiState,
        onUserClick = viewModel::onNavigateToUser,
        onRemoveFavoriteClick = viewModel::onRemoveFavoriteClick,
        onConfirmRemoveFavorite = viewModel::onConfirmRemoveFavorite,
        onDismissRemoveFavorite = viewModel::onDismissRemoveFavorite,
        onDismissError = viewModel::onDismissError,
        onUrlClick = onUrlClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesContent(
    favorites: LazyPagingItems<UserListItem>,
    uiState: FavoritesUiState,
    onUserClick: (UserListItem) -> Unit = {},
    onRemoveFavoriteClick: (UserListItem) -> Unit = {},
    onConfirmRemoveFavorite: () -> Unit = {},
    onDismissRemoveFavorite: () -> Unit = {},
    onDismissError: () -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.favorites_title)) },
            windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
        )
        when {
            favorites.loadState.refresh is LoadState.Loading -> Loading()
            favorites.itemCount == 0 -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.favorites_empty_message),
                    textAlign = TextAlign.Center,
                )
            }

            else -> UserList(
                pagingItems = favorites,
                onRetryClick = favorites::retry,
                onUserClick = onUserClick,
                onFavoriteClick = onRemoveFavoriteClick,
                onUrlClick = onUrlClick,
            )
        }
    }

    when (uiState) {
        is FavoritesUiState.ConfirmRemoval -> RemoveFavoriteDialog(
            username = uiState.item.username,
            onConfirm = onConfirmRemoveFavorite,
            onDismiss = onDismissRemoveFavorite,
        )
        is FavoritesUiState.RemovalError -> {
            val message = uiState.message.ifEmpty { stringResource(uiState.messageRes) }
            ErrorDialog(message = message, onDismiss = onDismissError)
        }
        FavoritesUiState.Idle -> Unit
    }
}

@Preview
@Composable
private fun FavoritesEmptyPreview() {
    Theme {
        val emptyPaging = flowOf(PagingData.empty<UserListItem>()).collectAsLazyPagingItems()
        FavoritesContent(
            favorites = emptyPaging,
            uiState = FavoritesUiState.Idle,
        )
    }
}

@Preview
@Composable
private fun FavoritesSuccessPreview() {
    Theme {
        val items = listOf(
            UserListItem(1, "JohnDoe", "", "https://github.com/johndoe"),
            UserListItem(2, "JaneSmith", "", "https://github.com/janesmith"),
        )
        val pagingItems = flowOf(PagingData.from(items)).collectAsLazyPagingItems()
        FavoritesContent(
            favorites = pagingItems,
            uiState = FavoritesUiState.Idle,
        )
    }
}
