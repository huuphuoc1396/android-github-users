@file:OptIn(ExperimentalMaterial3Api::class)

package com.tyme.github.users.feature.users.presentation.userlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.core.ui.components.ErrorDialog
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.feature.users.R
import com.tyme.github.users.feature.users.domain.model.UserModel
import com.tyme.github.users.feature.users.presentation.userlist.components.UserList

@Composable
fun UserListScreen(
    onNavigateToUser: (username: String, avatarUrl: String, url: String) -> Unit,
    onUrlClick: (String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel<UserListViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteUsernames by viewModel.favoriteUsernames.collectAsStateWithLifecycle()
    val pagingItems = viewModel.userPaging.collectAsLazyPagingItems()

    LaunchedEffect(pagingItems.loadState.refresh) {
        viewModel.onRefreshLoadState(pagingItems.loadState.refresh)
    }

    UserListContent(
        uiState = uiState,
        pagingItems = pagingItems,
        favoriteUsernames = favoriteUsernames,
        onRefresh = {
            pagingItems.refresh()
            viewModel.onRefreshTriggered()
        },
        onRetryClick = { pagingItems.retry() },
        onUserClick = { user -> onNavigateToUser(user.username, user.avatarUrl, user.url) },
        onFavoriteClick = viewModel::onFavoriteToggle,
        onUrlClick = onUrlClick,
        onDismissError = viewModel::dismissError,
    )
}

@Composable
private fun UserListContent(
    modifier: Modifier = Modifier,
    uiState: UserListUiState,
    pagingItems: LazyPagingItems<UserModel>,
    favoriteUsernames: Set<String>,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    onUserClick: (UserModel) -> Unit,
    onFavoriteClick: (UserModel) -> Unit,
    onUrlClick: (String) -> Unit,
    onDismissError: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.user_list_title)) },
        )

        when (uiState) {
            UserListUiState.Idle -> Unit
            UserListUiState.Loading -> Loading()
            is UserListUiState.Success -> PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
            ) {
                UserList(
                    pagingItems = pagingItems,
                    favoriteUsernames = favoriteUsernames,
                    onRetryClick = onRetryClick,
                    onUserClick = onUserClick,
                    onFavoriteClick = onFavoriteClick,
                    onUrlClick = onUrlClick,
                )
            }

            is UserListUiState.Error -> ErrorDialog(
                message = uiState.message.ifEmpty { stringResource(uiState.messageRes) },
                onDismiss = onDismissError,
            )
        }
    }
}
