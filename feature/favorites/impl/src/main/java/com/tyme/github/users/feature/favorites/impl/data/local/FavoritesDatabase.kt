package com.tyme.github.users.feature.favorites.impl.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val DATABASE_NAME = "github_favorites.db"

        fun create(context: Context): FavoritesDatabase =
            Room.databaseBuilder(context, FavoritesDatabase::class.java, DATABASE_NAME).build()
    }
}
