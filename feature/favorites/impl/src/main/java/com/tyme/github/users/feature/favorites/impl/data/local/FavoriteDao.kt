package com.tyme.github.users.feature.favorites.impl.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteDao {

    @Upsert
    suspend fun upsert(entity: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE username = :username")
    suspend fun deleteByUsername(username: String)

    @Query("SELECT COUNT(*) > 0 FROM FavoriteEntity WHERE username = :username")
    fun isFavorite(username: String): Flow<Boolean>

    @Query("SELECT * FROM FavoriteEntity ORDER BY rowid DESC")
    fun getAll(): Flow<List<FavoriteEntity>>
}
