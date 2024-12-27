package com.tyme.github.users.data.storages.databases.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tyme.github.users.data.storages.databases.entities.UserEntity

@Dao
internal interface UserDao {

    @Upsert
    suspend fun upsertAll(entities: List<UserEntity>)

    @Query("SELECT * FROM UserEntity")
    fun getPagingSource(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM UserEntity")
    suspend fun deleteAll()

    @Transaction
    suspend fun upsertAndDeleteAll(needToDelete: Boolean, entities: List<UserEntity>) {
        if (needToDelete) {
            deleteAll()
        }
        upsertAll(entities)
    }
}