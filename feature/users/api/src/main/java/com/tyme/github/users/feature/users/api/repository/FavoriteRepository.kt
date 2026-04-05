package com.tyme.github.users.feature.users.api.repository

import com.tyme.github.users.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addFavorite(user: UserModel)

    suspend fun removeFavorite(username: String)

    fun isFavorite(username: String): Flow<Boolean>

    fun getFavorites(): Flow<List<UserModel>>
}
