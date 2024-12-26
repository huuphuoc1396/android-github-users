package com.tyme.github.users.data.storages.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tyme.github.users.data.providers.SecretKeysProvider
import com.tyme.github.users.data.storages.databases.daos.UserDao
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets

@Database(
    entities = [UserEntity::class],
    version = 1,
)
internal abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "github_users.db"
    }

    class Factory(
        private val context: Context,
        private val isEncrypted: Boolean,
        private val secretKeysProvider: SecretKeysProvider
    ) {
        fun create(): UserDatabase {
            if (isEncrypted) {
                System.loadLibrary("sqlcipher")
                val password = secretKeysProvider.getDatabasePassword()
                val databaseFile = context.getDatabasePath(DATABASE_NAME)
                val factory = SupportOpenHelperFactory(password.toByteArray(StandardCharsets.UTF_8))
                return Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    databaseFile.absolutePath
                )
                    .openHelperFactory(factory)
                    .build()
            }
            return Room.databaseBuilder(
                context,
                UserDatabase::class.java,
                DATABASE_NAME,
            ).build()
        }
    }
}