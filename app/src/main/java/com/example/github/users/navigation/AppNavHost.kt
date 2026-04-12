package com.example.github.users.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.github.users.R
import com.example.github.users.core.navigation.AppNavigator
import com.example.github.users.core.navigation.NavigationEffects
import com.example.github.users.core.navigation.NavigationIntent
import com.example.github.users.core.ui.extension.openBrowser
import com.example.github.users.feature.favorites.navigation.FavoritesDestination
import com.example.github.users.feature.favorites.navigation.favoritesNavGraph
import com.example.github.users.feature.users.navigation.UserListDestination
import com.example.github.users.feature.users.navigation.usersNavGraph
import kotlinx.coroutines.launch

@Composable
internal fun AppNavHost(
    navigator: AppNavigator,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()

    val isUsersSelected = currentDestination?.hierarchy
        ?.any { destination -> destination.hasRoute(UserListDestination::class) } == true
    val isFavoritesSelected = currentDestination?.hierarchy
        ?.any { destination -> destination.hasRoute(FavoritesDestination::class) } == true
    val showBottomBar = isUsersSelected || isFavoritesSelected

    fun onTabClick(route: Any) = scope.launch {
        navigator.navigate(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToStartDestination = true,
                saveState = true,
                restoreState = true,
                launchSingleTop = true,
            )
        )
    }

    NavigationEffects(
        navigator = navigator,
        navController = navController,
        onOpenUrl = { url -> context.openBrowser(url) },
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = isUsersSelected,
                        onClick = { onTabClick(UserListDestination) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(R.string.bottom_nav_users)) },
                    )
                    NavigationBarItem(
                        selected = isFavoritesSelected,
                        onClick = { onTabClick(FavoritesDestination) },
                        icon = {
                            Icon(
                                imageVector = if (isFavoritesSelected) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                            )
                        },
                        label = { Text(text = stringResource(R.string.bottom_nav_favorites)) },
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = UserListDestination,
            modifier = Modifier.padding(paddingValues),
        ) {
            usersNavGraph()
            favoritesNavGraph()
        }
    }
}
