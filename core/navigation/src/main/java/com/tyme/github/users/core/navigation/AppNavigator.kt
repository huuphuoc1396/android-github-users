package com.tyme.github.users.core.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigator {

    val navigationActions: Flow<NavigationIntent>

    suspend fun navigate(intent: NavigationIntent)
}
