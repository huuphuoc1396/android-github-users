package com.example.github.users.feature.favorites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.github.users.feature.favorites.presentation.favorites.FavoritesScreen

fun NavGraphBuilder.favoritesNavGraph() {
    composable<FavoritesDestination> {
        FavoritesScreen()
    }
}
