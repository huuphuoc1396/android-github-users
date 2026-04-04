package com.tyme.github.users.feature.favorites.impl.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo("username")
    val username: String = "",
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("avatar_url")
    val avatarUrl: String = "",
    @ColumnInfo("url")
    val url: String = "",
)
