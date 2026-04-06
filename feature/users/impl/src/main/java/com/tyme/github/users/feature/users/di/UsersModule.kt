package com.tyme.github.users.feature.users.di

import android.content.Context
import com.tyme.github.users.core.database.AppDatabase
import com.tyme.github.users.core.database.dao.UserDao
import com.tyme.github.users.feature.users.data.local.UserPreferencesDataStore
import com.tyme.github.users.feature.users.data.local.UserPreferencesDataStoreImpl
import com.tyme.github.users.feature.users.data.remote.UserService
import com.tyme.github.users.feature.users.data.repository.UserRepositoryImpl
import com.tyme.github.users.feature.users.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.osipxd.security.crypto.encryptedPreferencesDataStore
import retrofit2.Retrofit
import javax.inject.Singleton

private val Context.preferences by encryptedPreferencesDataStore(name = "github_users.preferences_pb")

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UsersModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    companion object {

        @Provides
        @Singleton
        fun provideUserService(retrofit: Retrofit): UserService =
            retrofit.create(UserService::class.java)

        @Provides
        @Singleton
        fun provideUserPreferencesDataStore(
            @ApplicationContext context: Context,
        ): UserPreferencesDataStore = UserPreferencesDataStoreImpl(context.preferences)
    }
}
