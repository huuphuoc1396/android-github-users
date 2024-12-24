package com.tyme.github.users.ui.features.userdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tyme.github.users.extenstions.openBrowser
import com.tyme.github.users.ui.features.userdetails.components.UserDetailsContent
import com.tyme.github.users.ui.uistate.UiStateScreen

@Composable
internal fun UserDetailsScreen(
    navController: NavController,
    viewModel: UserDetailsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    UiStateScreen(
        viewModel = viewModel,
    ) { uiState ->
        UserDetailsContent(
            userDetails = uiState,
            onBackClick = { navController.navigateUp() },
            onBlogClick = { url -> context.openBrowser(url) },
        )
    }
}