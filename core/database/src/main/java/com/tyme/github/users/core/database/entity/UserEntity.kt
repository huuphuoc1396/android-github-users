package com.tyme.github.users.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("username")
    val username: String = "",
    @ColumnInfo("avatar_url")
    val avatarUrl: String = "",
    @ColumnInfo("url")
    val url: String = "",
    @ColumnInfo("is_favorite")
    val isFavorite: Boolean = false,
)
