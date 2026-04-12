package com.example.github.users.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.github.users.core.database.entity.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertAll(entities: List<UserEntity>)

    @Query("SELECT * FROM UserEntity")
    fun getPagingSource(): PagingSource<Int, UserEntity>

    @Query("SELECT id FROM UserEntity WHERE is_favorite = 1")
    suspend fun getFavoriteIds(): List<Int>

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
