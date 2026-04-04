package com.tyme.github.users

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationEffects
import com.tyme.github.users.feature.users.navigation.UserListDestination
import com.tyme.github.users.feature.users.navigation.usersNavGraph

@Composable
internal fun MainNavHost(
    navigator: AppNavigator,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavigationEffects(navigator = navigator, navController = navController)
    NavHost(
        navController = navController,
        startDestination = UserListDestination,
        modifier = modifier,
    ) {
        usersNavGraph(navController)
    }
}
