package com.tyme.github.users.data.di.modules

import com.android.template.data.storages.datastore.preferences.PreferencesDataStore
import com.android.template.data.storages.datastore.preferences.PreferencesDataStoreImpl
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