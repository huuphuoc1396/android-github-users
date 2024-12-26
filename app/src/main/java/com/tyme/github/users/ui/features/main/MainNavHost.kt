package com.tyme.github.users.ui.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.tyme.github.users.deeplink.DeepLinks
import com.tyme.github.users.ui.features.userdetails.UserDetailsScreen
import com.tyme.github.users.ui.features.userdetails.models.UserDetailsDestination
import com.tyme.github.users.ui.features.users.UserListScreen
import com.tyme.github.users.ui.features.users.models.UserListDestination

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
            UserListScreen(navController = navController)
        }

        composable<UserDetailsDestination>(
            deepLinks = listOf(
                navDeepLink<UserDetailsDestination>(DeepLinks.USER_DETAILS_PATH),
            ),
        ) {
            UserDetailsScreen(navController = navController)
        }
    }
}