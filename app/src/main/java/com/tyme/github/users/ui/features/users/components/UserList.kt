package com.tyme.github.users.ui.features.users.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.tyme.github.users.R
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.ui.theme.Theme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun UserList(
    pagingItems: LazyPagingItems<UserModel>,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onRetryClick: () -> Unit = {},
    onUserClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    val arrangement = Arrangement.spacedBy(12.dp)
    val gridCells = remember {
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
                user = user,
                onUserClick = onUserClick,
                onUrlClick = onUrlClick,
            )
        }

        when (pagingItems.loadState.append) {
            is LoadState.Error -> item {
                Button(
                    onClick = onRetryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
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

@Preview
@Composable
private fun UserListPreview() {
    Theme {
        val userList = MutableList(10) { index ->
            UserModel(
                id = index,
                username = "user$index",
                url = "https://www.github.com/user$index",
            )
        }.toImmutableList()
        val userPagingItems = flowOf(PagingData.from(userList)).collectAsLazyPagingItems()
        UserList(pagingItems = userPagingItems)
    }
}