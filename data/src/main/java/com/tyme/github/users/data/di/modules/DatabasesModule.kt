package com.tyme.github.users.data.di.modules

import android.content.Context
import com.tyme.github.users.data.BuildConfig
import com.tyme.github.users.data.providers.SecretKeysProvider
import com.tyme.github.users.data.storages.databases.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabasesModule {

    @Provides
    @Singleton
    fun provideUserDatabase(
        @ApplicationContext context: Context,
        secretKeysProvider: SecretKeysProvider,
    ): UserDatabase {
        return UserDatabase.Factory(
            context = context,
            isEncrypted = BuildConfig.DB_ENCRYPTION_ENABLED,
            secretKeysProvider = secretKeysProvider,
        ).create()
    }
}