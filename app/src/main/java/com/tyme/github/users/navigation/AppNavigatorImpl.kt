package com.tyme.github.users.navigation

import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

internal class AppNavigatorImpl @Inject constructor() : AppNavigator {

    private val _actions = Channel<NavigationIntent>(Channel.BUFFERED)
    override val navigationActions = _actions.receiveAsFlow()

    override suspend fun navigate(intent: NavigationIntent) {
        _actions.send(intent)
    }
}
