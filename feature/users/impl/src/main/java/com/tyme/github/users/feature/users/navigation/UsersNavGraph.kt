package com.tyme.github.users.feature.users.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.tyme.github.users.core.ui.extensions.openBrowser
import com.tyme.github.users.feature.users.presentation.userdetails.UserDetailsScreen
import com.tyme.github.users.feature.users.presentation.userlist.UserListScreen

fun NavGraphBuilder.usersNavGraph() {
    composable<UserListDestination>(
        deepLinks = listOf(
            navDeepLink<UserListDestination>(UsersDeepLinks.USER_LIST_PATH),
        ),
    ) {
        val context = LocalContext.current
        UserListScreen(
            onUrlClick = { url -> context.openBrowser(url) },
        )
    }

    composable<UserDetailsDestination>(
        deepLinks = listOf(
            navDeepLink<UserDetailsDestination>(UsersDeepLinks.USER_DETAILS_PATH),
        ),
    ) {
        val context = LocalContext.current
        UserDetailsScreen(
            onUrlClick = { url -> context.openBrowser(url) },
        )
    }
}
