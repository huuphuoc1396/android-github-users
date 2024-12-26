package com.tyme.github.users.ui.features.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.tyme.github.users.extenstions.openBrowser
import com.tyme.github.users.ui.features.userdetails.mappers.toUserDetailsDestination
import com.tyme.github.users.ui.features.users.components.UserListContent
import com.tyme.github.users.ui.uistate.UiStateScreen

@Composable
internal fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.userPaging.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(pagingItems.loadState.refresh) {
        if (pagingItems.loadState.refresh is LoadState.Error) {
            val error = (pagingItems.loadState.refresh as LoadState.Error).error
            viewModel.showError(error)
        }
        viewModel.setRefreshing(pagingItems.loadState.refresh is LoadState.Loading)
    }

    UiStateScreen(
        viewModel = viewModel,
    ) { uiState ->
        UserListContent(
            pagingItems = pagingItems,
            isRefreshing = uiState.isRefreshing,
            onUserClick = { user -> navController.navigate(user.toUserDetailsDestination()) },
            onUrlClick = { url -> context.openBrowser(url) },
            onRefresh = { pagingItems.refresh() },
        )
    }
}
