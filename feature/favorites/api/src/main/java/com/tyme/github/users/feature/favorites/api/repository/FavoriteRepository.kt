package com.tyme.github.users.feature.favorites.api.repository

import com.tyme.github.users.core.common.models.UserModel
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addFavorite(user: UserModel): Result<Unit>

    suspend fun removeFavorite(username: String): Result<Unit>

    fun isFavorite(username: String): Flow<Boolean>

    fun getFavorites(): Flow<List<UserModel>>
}
