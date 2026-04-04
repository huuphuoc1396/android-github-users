package com.tyme.github.users.feature.favorites.impl.data.repository

import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.favorites.impl.data.local.FavoriteDao
import com.tyme.github.users.feature.favorites.impl.data.mapper.toEntity
import com.tyme.github.users.feature.favorites.impl.data.mapper.toFavoriteUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao,
) : FavoriteRepository {

    override suspend fun addFavorite(user: FavoriteUser) {
        dao.upsert(user.toEntity())
    }

    override suspend fun removeFavorite(username: String) {
        dao.deleteByUsername(username)
    }

    override fun isFavorite(username: String): Flow<Boolean> =
        dao.isFavorite(username)

    override fun getFavorites(): Flow<List<FavoriteUser>> =
        dao.getAll().map { list -> list.map { it.toFavoriteUser() } }
}
