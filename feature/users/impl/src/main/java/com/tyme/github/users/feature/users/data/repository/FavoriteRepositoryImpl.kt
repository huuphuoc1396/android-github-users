package com.tyme.github.users.feature.users.data.repository

import com.tyme.github.users.feature.users.api.model.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.data.local.UserDao
import com.tyme.github.users.feature.users.data.mapper.toUserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoriteRepositoryImpl @Inject constructor(
    private val dao: UserDao,
) : FavoriteRepository {

    override suspend fun addFavorite(user: UserModel) {
        dao.setFavorite(user.username, true)
    }

    override suspend fun removeFavorite(username: String) {
        dao.setFavorite(username, false)
    }

    override fun isFavorite(username: String): Flow<Boolean> =
        dao.isFavorite(username)

    override fun getFavorites(): Flow<List<UserModel>> =
        dao.getFavorites().map { list -> list.map { it.toUserModel() } }
}
