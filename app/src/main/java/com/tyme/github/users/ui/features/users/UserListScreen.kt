package com.tyme.github.users.ui.features.users

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tyme.github.users.ui.features.users.components.UserListContent
import com.tyme.github.users.ui.uistate.UiStateScreen

@Composable
internal fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    UiStateScreen(
        viewModel = viewModel,
    ) { uiState ->
        UserListContent(
            userList = uiState,
            onUserClick = { /*TODO*/ }
        )
    }
}
