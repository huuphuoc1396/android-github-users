package com.tyme.github.users

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationEffects
import com.tyme.github.users.feature.favorites.impl.navigation.FavoritesDestination
import com.tyme.github.users.feature.favorites.impl.navigation.favoritesNavGraph
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import com.tyme.github.users.feature.users.navigation.UserListDestination
import com.tyme.github.users.feature.users.navigation.usersNavGraph

@Composable
internal fun MainNavHost(
    navigator: AppNavigator,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hierarchy?.none {
        it.hasRoute(UserDetailsDestination::class)
    } ?: true

    NavigationEffects(navigator = navigator, navController = navController)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(UserListDestination::class)
                        } == true,
                        onClick = {
                            navController.navigate(UserListDestination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                            )
                        },
                        label = { Text(text = stringResource(R.string.bottom_nav_users)) },
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(FavoritesDestination::class)
                        } == true,
                        onClick = {
                            navController.navigate(FavoritesDestination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val isFavoritesSelected = currentDestination?.hierarchy?.any {
                                it.hasRoute(FavoritesDestination::class)
                            } == true
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
            usersNavGraph(navController)
            favoritesNavGraph(
                navController = navController,
                onNavigateToUser = { username, avatarUrl, url ->
                    navController.navigate(UserDetailsDestination(username, avatarUrl, url))
                },
            )
        }
    }
}
