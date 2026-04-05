package com.tyme.github.users.feature.favorites.impl.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyme.github.users.core.ui.extensions.openBrowser
import com.tyme.github.users.feature.favorites.impl.presentation.favorites.FavoritesScreen

fun NavGraphBuilder.favoritesNavGraph() {
    composable<FavoritesDestination> {
        val context = LocalContext.current
        FavoritesScreen(
            onUrlClick = { url -> context.openBrowser(url) },
        )
    }
}
