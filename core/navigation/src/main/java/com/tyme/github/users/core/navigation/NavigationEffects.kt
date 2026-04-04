package com.tyme.github.users.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController

@Composable
fun NavigationEffects(
    navigator: AppNavigator,
    navController: NavHostController,
) {
    LaunchedEffect(navigator, navController) {
        navigator.navigationActions.collect { intent ->
            when (intent) {
                is NavigationIntent.NavigateTo -> navController.navigate(intent.route) {
                    intent.popUpTo?.let { route ->
                        popUpTo(route) { inclusive = intent.inclusive }
                    }
                }
                NavigationIntent.NavigateUp -> navController.navigateUp()
            }
        }
    }
}
