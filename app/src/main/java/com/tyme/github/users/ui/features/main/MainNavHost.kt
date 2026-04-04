package com.tyme.github.users.ui.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.tyme.github.users.deeplink.DeepLinks
import com.tyme.github.users.extenstions.openBrowser
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import com.tyme.github.users.feature.users.navigation.UserListDestination
import com.tyme.github.users.feature.users.presentation.userdetails.UserDetailsScreen
import com.tyme.github.users.feature.users.presentation.userlist.UserListScreen

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = UserListDestination,
        modifier = modifier,
    ) {

        composable<UserListDestination>(
            deepLinks = listOf(
                navDeepLink<UserListDestination>(DeepLinks.USER_LIST_PATH),
            ),
        ) {
            val context = LocalContext.current
            UserListScreen(
                onNavigateToUser = { username, avatarUrl, url ->
                    navController.navigate(UserDetailsDestination(username, avatarUrl, url))
                },
                onUrlClick = { url -> context.openBrowser(url) },
            )
        }

        composable<UserDetailsDestination>(
            deepLinks = listOf(
                navDeepLink<UserDetailsDestination>(DeepLinks.USER_DETAILS_PATH),
            ),
        ) {
            val context = LocalContext.current
            UserDetailsScreen(
                onNavigateBack = { navController.navigateUp() },
                onUrlClick = { url -> context.openBrowser(url) },
            )
        }
    }
}
