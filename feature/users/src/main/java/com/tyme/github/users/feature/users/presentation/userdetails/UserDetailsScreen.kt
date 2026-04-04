package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tyme.github.users.core.ui.components.ErrorDialog
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.feature.users.presentation.userdetails.components.UserDetailsScaffold

@Composable
fun UserDetailsScreen(
    onNavigateBack: () -> Unit,
    onUrlClick: (String) -> Unit,
    viewModel: UserDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    UserDetailsContent(
        uiState = uiState,
        isFavorite = isFavorite,
        onBackClick = onNavigateBack,
        onBlogClick = onUrlClick,
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onDismissError = viewModel::dismissError,
    )
}

@Composable
private fun UserDetailsContent(
    uiState: UserDetailUiState,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onBlogClick: (String) -> Unit,
    onFavoriteToggle: () -> Unit,
    onDismissError: () -> Unit,
) {
    when (uiState) {
        is UserDetailUiState.Idle -> Unit
        is UserDetailUiState.Loading -> Loading()
        is UserDetailUiState.Success -> UserDetailsScaffold(
            uiState = uiState,
            isFavorite = isFavorite,
            onBackClick = onBackClick,
            onBlogClick = onBlogClick,
            onFavoriteToggle = onFavoriteToggle,
        )
        is UserDetailUiState.Error -> ErrorDialog(
            message = uiState.message.ifEmpty { stringResource(uiState.messageRes) },
            onDismiss = onDismissError,
        )
    }
}
