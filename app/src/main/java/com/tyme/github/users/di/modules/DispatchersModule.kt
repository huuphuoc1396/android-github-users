package com.tyme.github.users.di.modules

import com.tyme.github.users.providers.DispatchersProvider
import com.tyme.github.users.providers.DispatchersProviderImpl
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