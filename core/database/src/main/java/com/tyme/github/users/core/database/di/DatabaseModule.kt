package com.tyme.github.users.core.database.di

import android.content.Context
import com.tyme.github.users.core.database.AppDatabase
import com.tyme.github.users.core.database.BuildConfig
import com.tyme.github.users.core.database.dao.FavoriteDao
import com.tyme.github.users.core.database.dao.UserDao
import com.tyme.github.users.core.security.providers.SecretKeysProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        secretKeysProvider: SecretKeysProvider,
    ): AppDatabase = AppDatabase.Factory(
        context = context,
        isEncrypted = BuildConfig.DB_ENCRYPTION_ENABLED,
        secretKeysProvider = secretKeysProvider,
    ).create()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}
