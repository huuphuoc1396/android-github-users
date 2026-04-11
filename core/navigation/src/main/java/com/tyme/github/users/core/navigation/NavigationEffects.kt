package com.tyme.github.users.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun NavigationEffects(
    navigator: AppNavigator,
    navController: NavHostController,
    onOpenUrl: (String) -> Unit = {},
) {
    LaunchedEffect(navigator, navController) {
        navigator.navigationActions.collect { intent ->
            when (intent) {
                is NavigationIntent.NavigateTo -> navController.navigate(intent.route) {
                    if (intent.popUpToStartDestination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = intent.saveState
                        }
                    } else {
                        intent.popUpTo?.let { route ->
                            popUpTo(route) { inclusive = intent.inclusive }
                        }
                    }
                    launchSingleTop = intent.launchSingleTop
                    restoreState = intent.restoreState
                }
                NavigationIntent.NavigateUp -> navController.navigateUp()
                is NavigationIntent.OpenUrl -> onOpenUrl(intent.url)
            }
        }
    }
}
