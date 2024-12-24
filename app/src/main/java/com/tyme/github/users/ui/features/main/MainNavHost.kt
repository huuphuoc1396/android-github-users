package com.tyme.github.users.ui.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

        composable<UserListDestination> {
            UserListScreen(navController = navController)
        }

        composable<UserDetailsDestination> {
            UserDetailsScreen()
        }
    }
}