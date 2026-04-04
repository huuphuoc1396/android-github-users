package com.tyme.github.users.feature.favorites.api.repository

import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addFavorite(user: FavoriteUser)

    suspend fun removeFavorite(username: String)

    fun isFavorite(username: String): Flow<Boolean>

    fun getFavorites(): Flow<List<FavoriteUser>>
}
