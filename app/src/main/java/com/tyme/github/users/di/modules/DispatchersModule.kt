package com.tyme.github.users.di.modules

import com.tyme.github.users.core.common.dispatcher.CoroutineDispatchers
import com.tyme.github.users.dispatcher.CoroutineDispatchersImpl
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
    fun bindCoroutineDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers
}
