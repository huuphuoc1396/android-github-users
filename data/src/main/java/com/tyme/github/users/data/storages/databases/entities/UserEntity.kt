package com.tyme.github.users.data.storages.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class UserEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("username")
    val username: String = "",
    @ColumnInfo("avatar_url")
    val avatarUrl: String = "",
    @ColumnInfo("url")
    val url: String = "",
)