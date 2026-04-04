package com.tyme.github.users.feature.users.presentation.userlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.core.ui.components.ErrorDialog
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.feature.users.domain.model.UserModel
import com.tyme.github.users.feature.users.presentation.userlist.components.UserListScaffold

@Composable
fun UserListScreen(
    onNavigateToUser: (username: String, avatarUrl: String, url: String) -> Unit,
    onUrlClick: (String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel<UserListViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.userPaging.collectAsLazyPagingItems()

    LaunchedEffect(pagingItems.loadState.refresh) {
        viewModel.onRefreshLoadState(pagingItems.loadState.refresh)
    }

    UserListContent(
        uiState = uiState,
        pagingItems = pagingItems,
        onRefresh = {
            pagingItems.refresh()
            viewModel.onRefreshTriggered()
        },
        onRetryClick = { pagingItems.retry() },
        onUserClick = { user -> onNavigateToUser(user.username, user.avatarUrl, user.url) },
        onUrlClick = onUrlClick,
        onDismissError = viewModel::dismissError,
    )
}

@Composable
private fun UserListContent(
    uiState: UserListUiState,
    pagingItems: LazyPagingItems<UserModel>,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    onUserClick: (UserModel) -> Unit,
    onUrlClick: (String) -> Unit,
    onDismissError: () -> Unit,
) {
    when (uiState) {
        UserListUiState.Idle -> Unit
        UserListUiState.Loading -> Loading()
        is UserListUiState.Success -> UserListScaffold(
            pagingItems = pagingItems,
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            onRetryClick = onRetryClick,
            onUserClick = onUserClick,
            onUrlClick = onUrlClick,
        )
        is UserListUiState.Error -> ErrorDialog(
            message = uiState.message,
            onDismiss = onDismissError,
        )
    }
}
