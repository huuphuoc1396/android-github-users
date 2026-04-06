package com.tyme.github.users.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.tyme.github.users.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM UserEntity WHERE is_favorite = 1")
    fun getFavorites(): Flow<List<UserEntity>>

    @Query("UPDATE UserEntity SET is_favorite = :isFavorite WHERE username = :username")
    suspend fun setFavorite(username: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) > 0 FROM UserEntity WHERE username = :username AND is_favorite = 1")
    fun isFavorite(username: String): Flow<Boolean>
}
