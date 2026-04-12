package com.example.github.users.feature.favorites.domain.repository

import com.example.github.users.core.common.model.UserModel
import kotlinx.coroutines.flow.Flow

internal interface FavoriteRepository {

    suspend fun addFavorite(user: UserModel): Result<Unit>

    suspend fun removeFavorite(username: String): Result<Unit>

    fun isFavorite(username: String): Flow<Boolean>

    fun getFavorites(): Flow<List<UserModel>>
}