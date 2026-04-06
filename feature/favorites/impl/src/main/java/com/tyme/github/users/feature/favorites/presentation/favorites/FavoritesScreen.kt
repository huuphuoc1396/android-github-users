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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.core.ui.components.RemoveFavoriteDialog
import com.tyme.github.users.core.ui.components.UserList
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.favorites.impl.R

@Composable
fun FavoritesScreen(
    onUrlClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesContent(
        uiState = uiState,
        onUserClick = viewModel::onNavigateToUser,
        onRemoveFavoriteClick = viewModel::onRemoveFavoriteClick,
        onConfirmRemoveFavorite = viewModel::onConfirmRemoveFavorite,
        onDismissRemoveFavorite = viewModel::onDismissRemoveFavorite,
        onUrlClick = onUrlClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesContent(
    uiState: FavoritesUiState,
    onUserClick: (UserListItem) -> Unit = {},
    onRemoveFavoriteClick: (UserListItem) -> Unit = {},
    onConfirmRemoveFavorite: () -> Unit = {},
    onDismissRemoveFavorite: () -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.favorites_title)) },
            windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
        )
        when (uiState) {
            FavoritesUiState.Loading -> Loading()
            FavoritesUiState.Empty -> Box(
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

            is FavoritesUiState.Success -> {
                UserList(
                    users = uiState.favorites,
                    onUserClick = onUserClick,
                    onFavoriteClick = onRemoveFavoriteClick,
                    onUrlClick = onUrlClick,
                )
                if (uiState.pendingRemoval != null) {
                    RemoveFavoriteDialog(
                        username = uiState.pendingRemoval.username,
                        onConfirm = onConfirmRemoveFavorite,
                        onDismiss = onDismissRemoveFavorite,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoritesEmptyPreview() {
    Theme {
        FavoritesContent(uiState = FavoritesUiState.Empty)
    }
}

@Preview
@Composable
private fun FavoritesSuccessPreview() {
    Theme {
        FavoritesContent(
            uiState = FavoritesUiState.Success(
                favorites = listOf(
                    UserListItem(1, "JohnDoe", "", "https://github.com/johndoe"),
                    UserListItem(2, "JaneSmith", "", "https://github.com/janesmith"),
                ),
            ),
        )
    }
}
