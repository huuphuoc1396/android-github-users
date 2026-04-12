package com.tyme.github.users.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tyme.github.users.core.database.dao.FavoriteDao
import com.tyme.github.users.core.database.dao.UserDao
import com.tyme.github.users.core.database.entity.UserEntity
import com.tyme.github.users.core.security.AppSecrets
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val DATABASE_NAME = "github_users.db"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE UserEntity ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
            }
        }
    }

    class Factory(
        private val context: Context,
        private val isEncrypted: Boolean,
        private val appSecrets: AppSecrets,
    ) {
        fun create(): AppDatabase {
            if (isEncrypted) {
                System.loadLibrary("sqlcipher")
                val password = appSecrets.databasePassword
                val databaseFile = context.getDatabasePath(DATABASE_NAME)
                val factory = SupportOpenHelperFactory(password.toByteArray(StandardCharsets.UTF_8))
                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    databaseFile.absolutePath,
                )
                    .openHelperFactory(factory)
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME,
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
