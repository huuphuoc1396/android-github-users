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
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserListContent(
    pagingItems: LazyPagingItems<UserModel>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onUserClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
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
                onUserClick = onUserClick,
                onUrlClick = onUrlClick,
            )
        }
    }
}

@Preview
@Composable
private fun UserListContentPreview() {
    Theme {
        UserListContent(
            pagingItems = flowOf(PagingData.empty<UserModel>()).collectAsLazyPagingItems(),
        )
    }
}