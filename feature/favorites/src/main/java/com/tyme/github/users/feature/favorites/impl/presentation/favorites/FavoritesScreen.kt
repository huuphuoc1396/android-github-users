package com.tyme.github.users.feature.favorites.impl.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.core.ui.components.RemoveFavoriteDialog
import com.tyme.github.users.core.ui.components.UserCard
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.users.api.model.UserModel
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
    onUserClick: (UserModel) -> Unit = {},
    onRemoveFavoriteClick: (UserModel) -> Unit = {},
    onConfirmRemoveFavorite: () -> Unit = {},
    onDismissRemoveFavorite: () -> Unit = {},
    onUrlClick: (String) -> Unit = {},
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.favorites_title)) },
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
                val gridCells = remember(windowSizeClass) {
                    if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                        GridCells.Fixed(1)
                    } else {
                        GridCells.Fixed(2)
                    }
                }
                val arrangement = Arrangement.spacedBy(12.dp)
                LazyVerticalGrid(
                    columns = gridCells,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = arrangement,
                    horizontalArrangement = arrangement,
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(uiState.favorites, key = { it.username }) { user ->
                        UserCard(
                            username = user.username,
                            avatarUrl = user.avatarUrl,
                            url = user.url,
                            isFavorite = true,
                            onUserClick = { onUserClick(user) },
                            onFavoriteClick = { onRemoveFavoriteClick(user) },
                            onUrlClick = onUrlClick,
                        )
                    }
                }
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
                    UserModel(1, "JohnDoe", "", "https://github.com/johndoe"),
                    UserModel(2, "JaneSmith", "", "https://github.com/janesmith"),
                ),
            ),
        )
    }
}
