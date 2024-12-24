package com.tyme.github.users.di.modules

import com.tyme.github.users.providers.dispatchers.DispatchersProvider
import com.tyme.github.users.providers.dispatchers.DispatchersProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DispatchersModule {

    @Binds
    @Singleton
    fun bindDispatchersProvider(impl: DispatchersProviderImpl): DispatchersProvider
}