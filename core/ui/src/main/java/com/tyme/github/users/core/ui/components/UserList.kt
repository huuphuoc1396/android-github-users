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
import androidx.compose.runtime.Composable
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
import com.tyme.github.users.core.ui.R
import com.tyme.github.users.core.ui.theme.Theme
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserList(
    pagingItems: LazyPagingItems<UserListItem>,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit = {},
    onUserClick: (UserListItem) -> Unit = {},
    onFavoriteClick: (UserListItem) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    val arrangement = Arrangement.spacedBy(12.dp)
    LazyVerticalGrid(
        columns = GridCells.Adaptive(256.dp),
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
                item = user,
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
    items: List<UserListItem>,
    modifier: Modifier = Modifier,
    onUserClick: (UserListItem) -> Unit = {},
    onFavoriteClick: (UserListItem) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    val arrangement = Arrangement.spacedBy(12.dp)
    LazyVerticalGrid(
        columns = GridCells.Adaptive(256.dp),
        modifier = modifier,
        verticalArrangement = arrangement,
        horizontalArrangement = arrangement,
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = items,
            key = { it.id },
        ) { user ->
            UserCard(
                item = user,
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
            UserListItem(
                id = index,
                username = "user$index",
                url = "https://www.github.com/user$index",
            )
        }
        val pagingItems = flowOf(PagingData.from(userList)).collectAsLazyPagingItems()
        UserList(pagingItems = pagingItems)
    }
}
