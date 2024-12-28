package com.tyme.github.users.ui.features.users.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.R
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.ui.theme.Theme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserListContent(
    pagingItems: LazyPagingItems<UserModel>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onUserClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.user_list_title)) },
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(paddingValues)
        ) {
            UserList(
                pagingItems = pagingItems,
                modifier = Modifier.fillMaxSize(),
                onRetryClick = onRetryClick,
                onUserClick = onUserClick,
                onUrlClick = onUrlClick,
            )
        }
    }
}

@Preview
@Composable
private fun UserListContentPreview() {
    val userList = MutableList(10) { index ->
        UserModel(
            id = index,
            username = "user$index",
            url = "https://www.github.com/user$index",
        )
    }
    Theme {
        UserListContent(
            pagingItems = flowOf(PagingData.from(userList)).collectAsLazyPagingItems(),
        )
    }
}