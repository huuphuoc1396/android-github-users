package com.tyme.github.users.core.database.di

import android.content.Context
import com.tyme.github.users.core.database.AppDatabase
import com.tyme.github.users.core.database.BuildConfig
import com.tyme.github.users.core.database.dao.FavoriteDao
import com.tyme.github.users.core.database.dao.UserDao
import com.tyme.github.users.core.security.AppSecrets
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        appSecrets: AppSecrets,
    ): AppDatabase = AppDatabase.Factory(
        context = context,
        isEncrypted = BuildConfig.DB_ENCRYPTION_ENABLED,
        appSecrets = appSecrets,
    ).create()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}
