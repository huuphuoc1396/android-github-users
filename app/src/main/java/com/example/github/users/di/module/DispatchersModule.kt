package com.example.github.users.di.module

import com.example.github.users.core.common.dispatcher.CoroutineDispatchers
import com.example.github.users.dispatcher.CoroutineDispatchersImpl
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
