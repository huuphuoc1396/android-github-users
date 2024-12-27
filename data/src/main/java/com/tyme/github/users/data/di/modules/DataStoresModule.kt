package com.tyme.github.users.data.di.modules

import com.tyme.github.users.data.storages.datastores.PreferencesDataStore
import com.tyme.github.users.data.storages.datastores.PreferencesDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataStoresModule {

    @Binds
    fun bindPreferencesDataStore(impl: PreferencesDataStoreImpl): PreferencesDataStore
}