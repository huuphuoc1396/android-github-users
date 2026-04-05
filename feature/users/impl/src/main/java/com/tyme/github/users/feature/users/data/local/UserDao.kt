package com.tyme.github.users.feature.users.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Upsert
    suspend fun upsertAll(entities: List<UserEntity>)

    @Query("SELECT * FROM UserEntity")
    fun getPagingSource(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM UserEntity WHERE is_favorite = 1")
    fun getFavorites(): Flow<List<UserEntity>>

    @Query("SELECT id FROM UserEntity WHERE is_favorite = 1")
    suspend fun getFavoriteIds(): List<Int>

    @Query("UPDATE UserEntity SET is_favorite = :isFavorite WHERE username = :username")
    suspend fun setFavorite(username: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) > 0 FROM UserEntity WHERE username = :username AND is_favorite = 1")
    fun isFavorite(username: String): Flow<Boolean>

    @Query("DELETE FROM UserEntity WHERE is_favorite = 0")
    suspend fun deleteNonFavorites()

    @Transaction
    suspend fun upsertAndDeleteAll(needToDelete: Boolean, entities: List<UserEntity>) {
        val favoriteIds = getFavoriteIds().toSet()
        if (needToDelete) {
            deleteNonFavorites()
        }
        upsertAll(entities.map { entity ->
            entity.copy(isFavorite = entity.id in favoriteIds)
        })
    }
}
