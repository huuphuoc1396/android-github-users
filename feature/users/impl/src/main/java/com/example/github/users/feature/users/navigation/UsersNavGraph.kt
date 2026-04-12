package com.example.github.users.feature.users.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.github.users.feature.users.presentation.userdetails.UserDetailsScreen
import com.example.github.users.feature.users.presentation.userlist.UserListScreen

fun NavGraphBuilder.usersNavGraph() {
    composable<UserListDestination>(
        deepLinks = listOf(
            navDeepLink<UserListDestination>(UsersDeepLinks.USER_LIST_PATH),
        ),
    ) {
        UserListScreen()
    }

    composable<UserDetailsDestination>(
        deepLinks = listOf(
            navDeepLink<UserDetailsDestination>(UsersDeepLinks.USER_DETAILS_PATH),
        ),
    ) {
        UserDetailsScreen()
    }
}
