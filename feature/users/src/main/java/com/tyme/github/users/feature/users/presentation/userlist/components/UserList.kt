package com.tyme.github.users.feature.users.presentation.userlist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.users.R
import com.tyme.github.users.feature.users.domain.model.UserModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserList(
    pagingItems: LazyPagingItems<UserModel>,
    favoriteUsernames: Set<String>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onUserClick: (UserModel) -> Unit = {},
    onFavoriteClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.user_list_title)) },
        )
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            UserList(
                pagingItems = pagingItems,
                favoriteUsernames = favoriteUsernames,
                modifier = Modifier.fillMaxSize(),
                onRetryClick = onRetryClick,
                onUserClick = onUserClick,
                onFavoriteClick = onFavoriteClick,
                onUrlClick = onUrlClick,
            )
        }
    }
}

@Preview
@Composable
private fun UserListPreview() {
    val userList = MutableList(10) { index ->
        UserModel(
            id = index,
            username = "user$index",
            url = "https://www.github.com/user$index",
        )
    }
    Theme {
        UserList(
            pagingItems = flowOf(PagingData.from(userList)).collectAsLazyPagingItems(),
            favoriteUsernames = emptySet(),
        )
    }
}
