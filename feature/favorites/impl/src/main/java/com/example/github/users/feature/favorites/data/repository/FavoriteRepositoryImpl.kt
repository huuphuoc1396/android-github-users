package com.example.github.users.feature.favorites.data.repository

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.core.database.dao.FavoriteDao
import com.example.github.users.feature.favorites.data.mapper.toUserModel
import com.example.github.users.feature.favorites.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao,
) : FavoriteRepository {

    override suspend fun addFavorite(user: UserModel): Result<Unit> = runCatching {
        dao.setFavorite(user.username, true)
    }

    override suspend fun removeFavorite(username: String): Result<Unit> = runCatching {
        dao.setFavorite(username, false)
    }

    override fun isFavorite(username: String): Flow<Boolean> =
        dao.isFavorite(username)

    override fun getFavorites(): Flow<List<UserModel>> =
        dao.getFavorites().map { list -> list.map { it.toUserModel() } }
}
