package com.tyme.github.users.data.storages.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tyme.github.users.data.storages.databases.daos.UserDao
import com.tyme.github.users.data.storages.databases.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
)
internal abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "github_users.db"

        fun create(context: Context): UserDatabase {
            return Room.databaseBuilder(
                context,
                UserDatabase::class.java,
                DATABASE_NAME,
            ).build()
        }
    }
}