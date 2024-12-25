package com.tyme.github.users.ui.features.users

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tyme.github.users.extenstions.openBrowser
import com.tyme.github.users.ui.features.userdetails.models.UserDetailsDestination
import com.tyme.github.users.ui.features.users.components.UserListContent
import com.tyme.github.users.ui.uistate.UiStateScreen

@Composable
internal fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    UiStateScreen(
        viewModel = viewModel,
    ) { uiState ->
        UserListContent(
            userList = uiState,
            onUserClick = { user -> navController.navigate(UserDetailsDestination(user.username)) },
            onUrlClick = { url -> context.openBrowser(url) },
            onRefresh = viewModel::refresh,
        )
    }
}
