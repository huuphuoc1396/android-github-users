package com.example.github.users.feature.favorites.presentation.favorites

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
import com.example.github.users.core.ui.component.ErrorDialog
import com.example.github.users.core.ui.component.RemoveFavoriteDialog
import com.example.github.users.core.ui.component.UserList
import com.example.github.users.core.ui.component.UserListItem
import com.example.github.users.core.ui.extension.asString
import com.example.github.users.core.ui.theme.Theme
import com.example.github.users.feature.favorites.impl.R

@Composable
internal fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    FavoritesContent(
        favorites = favorites,
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesContent(
    favorites: List<UserListItem>,
    uiState: FavoritesUiState,
    onAction: (FavoritesUiAction) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.favorites_title)) },
            windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
        )
        when {
            favorites.isEmpty() -> Box(
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
                items = favorites,
                onUserClick = { onAction(FavoritesUiAction.UserClick(it)) },
                onFavoriteClick = { onAction(FavoritesUiAction.RemoveFavoriteClick(it)) },
                onUrlClick = { onAction(FavoritesUiAction.UrlClick(it)) },
            )
        }
    }

    when (uiState) {
        is FavoritesUiState.ConfirmRemoval -> RemoveFavoriteDialog(
            username = uiState.item.username,
            onConfirm = { onAction(FavoritesUiAction.ConfirmRemoveFavorite) },
            onDismiss = { onAction(FavoritesUiAction.DismissRemoveFavorite) },
        )
        is FavoritesUiState.RemovalError -> ErrorDialog(
            message = uiState.message.asString(),
            onDismiss = { onAction(FavoritesUiAction.DismissError) },
        )
        is FavoritesUiState.LoadError -> ErrorDialog(
            message = uiState.message.asString(),
            onDismiss = { onAction(FavoritesUiAction.DismissError) },
        )
        FavoritesUiState.Idle -> Unit
    }
}

@Preview
@Composable
private fun FavoritesEmptyPreview() {
    Theme {
        FavoritesContent(
            favorites = emptyList(),
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
        FavoritesContent(
            favorites = items,
            uiState = FavoritesUiState.Idle,
        )
    }
}
