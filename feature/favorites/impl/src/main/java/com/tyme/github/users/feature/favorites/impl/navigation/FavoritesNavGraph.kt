package com.tyme.github.users.feature.favorites.impl.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.tyme.github.users.core.ui.extensions.openBrowser
import com.tyme.github.users.feature.favorites.impl.presentation.favorites.FavoritesScreen

fun NavGraphBuilder.favoritesNavGraph(
    navController: NavHostController,
    onNavigateToUser: (username: String, avatarUrl: String, url: String) -> Unit,
) {
    composable<FavoritesDestination> {
        val context = LocalContext.current
        FavoritesScreen(
            onNavigateToUser = onNavigateToUser,
            onUrlClick = { url -> context.openBrowser(url) },
        )
    }
}
