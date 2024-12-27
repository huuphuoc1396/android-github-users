package com.tyme.github.users.data.di.modules

import android.content.Context
import com.tyme.github.users.data.storages.datastores.PreferencesDataStore
import com.tyme.github.users.data.storages.datastores.PreferencesDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.osipxd.security.crypto.encryptedPreferencesDataStore
import javax.inject.Singleton

private val Context.preferences by encryptedPreferencesDataStore(name = "github_users.preferences_pb")

@Module
@InstallIn(SingletonComponent::class)
internal class DataStoresModule {

    @Provides
    @Singleton
    fun bindPreferencesDataStore(@ApplicationContext context: Context): PreferencesDataStore {
        return PreferencesDataStoreImpl(context.preferences)
    }
}