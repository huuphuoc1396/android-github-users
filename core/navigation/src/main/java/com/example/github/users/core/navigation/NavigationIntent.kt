package com.example.github.users.core.navigation

sealed interface NavigationIntent {

    data class NavigateTo(
        val route: Any,
        val popUpToStartDestination: Boolean = false,
        val saveState: Boolean = false,
        val restoreState: Boolean = false,
        val launchSingleTop: Boolean = false,
        val popUpTo: Any? = null,
        val inclusive: Boolean = false,
    ) : NavigationIntent

    data object NavigateUp : NavigationIntent

    data class OpenUrl(val url: String) : NavigationIntent
}
