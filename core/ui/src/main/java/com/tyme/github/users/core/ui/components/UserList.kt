package com.tyme.github.users.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.tyme.github.users.core.ui.R
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.domain.models.UserModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserList(
    pagingItems: LazyPagingItems<UserModel>,
    favoriteUsernames: Set<String>,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onRetryClick: () -> Unit = {},
    onUserClick: (UserModel) -> Unit = {},
    onFavoriteClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    val arrangement = Arrangement.spacedBy(12.dp)
    val gridCells = remember(windowSizeClass) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            GridCells.Fixed(1)
        } else {
            GridCells.Fixed(2)
        }
    }
    LazyVerticalGrid(
        columns = gridCells,
        modifier = modifier,
        verticalArrangement = arrangement,
        horizontalArrangement = arrangement,
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { user -> user.id },
        ) { index ->
            val user = pagingItems[index] ?: return@items
            UserCard(
                username = user.username,
                avatarUrl = user.avatarUrl,
                url = user.url,
                isFavorite = user.username in favoriteUsernames,
                onUserClick = { onUserClick(user) },
                onFavoriteClick = { onFavoriteClick(user) },
                onUrlClick = onUrlClick,
            )
        }

        when (pagingItems.loadState.append) {
            is LoadState.Error -> item {
                Button(
                    onClick = onRetryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                ) {
                    Text(text = stringResource(R.string.button_retry))
                }
            }

            is LoadState.Loading -> item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
            }

            else -> {}
        }
    }
}

@Composable
fun UserList(
    users: List<UserModel>,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onUserClick: (UserModel) -> Unit = {},
    onFavoriteClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    val arrangement = Arrangement.spacedBy(12.dp)
    val gridCells = remember(windowSizeClass) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            GridCells.Fixed(1)
        } else {
            GridCells.Fixed(2)
        }
    }
    LazyVerticalGrid(
        columns = gridCells,
        modifier = modifier,
        verticalArrangement = arrangement,
        horizontalArrangement = arrangement,
        contentPadding = PaddingValues(16.dp),
    ) {
        items(users, key = { it.id }) { user ->
            UserCard(
                username = user.username,
                avatarUrl = user.avatarUrl,
                url = user.url,
                isFavorite = user.isFavorite,
                onUserClick = { onUserClick(user) },
                onFavoriteClick = { onFavoriteClick(user) },
                onUrlClick = onUrlClick,
            )
        }
    }
}

@Preview
@Composable
private fun UserListPagingPreview() {
    Theme {
        val userList = MutableList(10) { index ->
            UserModel(
                id = index,
                username = "user$index",
                url = "https://www.github.com/user$index",
            )
        }
        val pagingItems = flowOf(PagingData.from(userList)).collectAsLazyPagingItems()
        UserList(pagingItems = pagingItems, favoriteUsernames = emptySet())
    }
}

@Preview
@Composable
private fun UserListStaticPreview() {
    Theme {
        val users = List(5) { index ->
            UserModel(
                id = index,
                username = "user$index",
                url = "https://www.github.com/user$index",
                isFavorite = true,
            )
        }
        UserList(users = users)
    }
}
