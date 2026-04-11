package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tyme.github.users.core.ui.components.BackButton
import com.tyme.github.users.core.ui.components.ErrorDialog
import com.tyme.github.users.core.ui.extensions.asString
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.core.ui.components.RemoveFavoriteDialog
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.users.R
import com.tyme.github.users.feature.users.presentation.userdetails.components.UserDetails

@Composable
fun UserDetailsScreen(
    viewModel: UserDetailsViewModel = hiltViewModel<UserDetailsViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    UserDetailsContent(
        uiState = uiState,
        isFavorite = isFavorite,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDetailsContent(
    uiState: UserDetailUiState,
    isFavorite: Boolean,
    onAction: (UserDetailsUiAction) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.user_details_title)) },
            navigationIcon = { BackButton(onClick = { onAction(UserDetailsUiAction.NavigateBack) }) },
            windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
            actions = {
                if (uiState is UserDetailUiState.Success) {
                    IconButton(onClick = { onAction(UserDetailsUiAction.FavoriteToggle) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            },
        )
        when (uiState) {
            is UserDetailUiState.Idle -> Unit
            is UserDetailUiState.Loading -> Loading()
            is UserDetailUiState.Success -> UserDetails(
                uiState = uiState,
                onBlogClick = { url -> onAction(UserDetailsUiAction.BlogClick(url)) },
                modifier = Modifier.fillMaxSize(),
            )
            is UserDetailUiState.Error -> ErrorDialog(
                message = uiState.message.asString(),
                onDismiss = { onAction(UserDetailsUiAction.DismissError) },
            )
        }
    }
    if (uiState is UserDetailUiState.Success && uiState.showRemoveConfirmDialog) {
        RemoveFavoriteDialog(
            username = uiState.username,
            onConfirm = { onAction(UserDetailsUiAction.ConfirmRemoveFavorite) },
            onDismiss = { onAction(UserDetailsUiAction.DismissRemoveFavorite) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsContentPreview() {
    Theme {
        UserDetailsContent(
            uiState = UserDetailUiState.Success(
                username = "mojombo",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                country = "San Francisco",
                followers = "20k",
                following = "11k",
                url = "https://github.com/mojombo"
            ),
            isFavorite = true,
            onAction = {},
        )
    }
}
