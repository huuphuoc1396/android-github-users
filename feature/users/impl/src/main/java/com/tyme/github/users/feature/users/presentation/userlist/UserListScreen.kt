@file:OptIn(ExperimentalMaterial3Api::class)

package com.tyme.github.users.feature.users.presentation.userlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Surface
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.core.common.models.errors.UiText
import com.tyme.github.users.core.ui.components.ErrorDialog
import com.tyme.github.users.core.ui.components.Loading
import com.tyme.github.users.core.ui.extensions.asString
import com.tyme.github.users.core.ui.components.RemoveFavoriteDialog
import com.tyme.github.users.core.ui.components.UserList
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.users.R
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserListScreen(
    onUrlClick: (String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel<UserListViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.userPaging.collectAsLazyPagingItems()

    fun onRefresh() {
        pagingItems.refresh()
        viewModel.onRefreshTriggered()
    }

    LaunchedEffect(pagingItems.loadState.refresh) {
        viewModel.onRefreshLoadState(pagingItems.loadState.refresh)
    }

    UserListContent(
        uiState = uiState,
        pagingItems = pagingItems,
        onRefresh = ::onRefresh,
        onRetryClick = { pagingItems.retry() },
        onUserClick = viewModel::onNavigateToUser,
        onFavoriteClick = viewModel::onFavoriteClick,
        onConfirmRemoveFavorite = viewModel::onConfirmRemoveFavorite,
        onDismissRemoveFavorite = viewModel::onDismissRemoveFavorite,
        onUrlClick = onUrlClick,
        onDismissError = viewModel::dismissError,
    )
}

@Composable
private fun UserListContent(
    modifier: Modifier = Modifier,
    uiState: UserListUiState,
    pagingItems: LazyPagingItems<UserListItem>,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    onUserClick: (UserListItem) -> Unit,
    onFavoriteClick: (UserListItem) -> Unit,
    onConfirmRemoveFavorite: () -> Unit,
    onDismissRemoveFavorite: () -> Unit,
    onUrlClick: (String) -> Unit,
    onDismissError: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.user_list_title)) },
            windowInsets = TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
        )

        when (uiState) {
            UserListUiState.Idle -> Unit
            UserListUiState.Loading -> Loading()
            is UserListUiState.Success -> {
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onRefresh,
                ) {
                    UserList(
                        pagingItems = pagingItems,
                        onRetryClick = onRetryClick,
                        onUserClick = onUserClick,
                        onFavoriteClick = onFavoriteClick,
                        onUrlClick = onUrlClick,
                    )
                }
                if (uiState.pendingRemoval != null) {
                    RemoveFavoriteDialog(
                        username = uiState.pendingRemoval.username,
                        onConfirm = onConfirmRemoveFavorite,
                        onDismiss = onDismissRemoveFavorite,
                    )
                }
            }

            is UserListUiState.Error -> ErrorDialog(
                message = uiState.message.asString(),
                onDismiss = onDismissError,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListContentSuccessPreview() {
    Theme {
        Surface {
            val userList = List(10) { index ->
                UserListItem(
                    id = index,
                    username = "User $index",
                    avatarUrl = "https://avatars.githubusercontent.com/u/${index + 1}?v=4",
                    url = "https://github.com/user$index"
                )
            }
            val pagingItems = flowOf(
                PagingData.from(
                    userList,
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false)
                    )
                )
            ).collectAsLazyPagingItems()
            UserListContent(
                uiState = UserListUiState.Success(),
                pagingItems = pagingItems,
                onRefresh = {},
                onRetryClick = {},
                onUserClick = {},
                onFavoriteClick = {},
                onConfirmRemoveFavorite = {},
                onDismissRemoveFavorite = {},
                onUrlClick = {},
                onDismissError = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListContentLoadingPreview() {
    Theme {
        Surface {
            val pagingItems =
                flowOf(PagingData.empty<UserListItem>()).collectAsLazyPagingItems()
            UserListContent(
                uiState = UserListUiState.Loading,
                pagingItems = pagingItems,
                onRefresh = {},
                onRetryClick = {},
                onUserClick = {},
                onFavoriteClick = {},
                onConfirmRemoveFavorite = {},
                onDismissRemoveFavorite = {},
                onUrlClick = {},
                onDismissError = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListContentErrorPreview() {
    Theme {
        val pagingItems =
            flowOf(PagingData.empty<UserListItem>()).collectAsLazyPagingItems()
        Surface {
            UserListContent(
                uiState = UserListUiState.Error(UiText.Dynamic("An error occurred")),
                pagingItems = pagingItems,
                onRefresh = {},
                onRetryClick = {},
                onUserClick = {},
                onFavoriteClick = {},
                onConfirmRemoveFavorite = {},
                onDismissRemoveFavorite = {},
                onUrlClick = {},
                onDismissError = {},
            )
        }
    }
}
