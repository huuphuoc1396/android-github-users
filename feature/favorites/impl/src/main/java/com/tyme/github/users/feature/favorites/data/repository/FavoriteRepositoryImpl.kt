package com.tyme.github.users.feature.favorites.data.repository

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.database.dao.FavoriteDao
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.favorites.data.mapper.toUserModel
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
