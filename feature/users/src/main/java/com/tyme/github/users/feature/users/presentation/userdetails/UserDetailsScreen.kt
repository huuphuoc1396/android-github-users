package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    UserDetailsContent(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onBlogClick = onUrlClick,
        onDismissError = viewModel::dismissError,
    )
}

@Composable
private fun UserDetailsContent(
    uiState: UserDetailUiState,
    onBackClick: () -> Unit,
    onBlogClick: (String) -> Unit,
    onDismissError: () -> Unit,
) {
    when (uiState) {
        is UserDetailUiState.Idle -> Unit
        is UserDetailUiState.Loading -> Loading()
        is UserDetailUiState.Success -> UserDetailsScaffold(
            uiState = uiState,
            onBackClick = onBackClick,
            onBlogClick = onBlogClick,
        )
        is UserDetailUiState.Error -> ErrorDialog(
            message = uiState.message,
            onDismiss = onDismissError,
        )
    }
}
