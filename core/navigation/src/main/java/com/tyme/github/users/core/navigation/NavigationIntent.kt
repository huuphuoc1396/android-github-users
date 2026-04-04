package com.tyme.github.users.core.navigation

sealed interface NavigationIntent {

    data class NavigateTo(
        val route: Any,
        val popUpTo: Any? = null,
        val inclusive: Boolean = false,
    ) : NavigationIntent

    data object NavigateUp : NavigationIntent
}
