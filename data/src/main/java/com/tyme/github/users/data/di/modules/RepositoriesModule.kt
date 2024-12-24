package com.tyme.github.users.data.di.modules

import com.tyme.github.users.data.repositories.users.UserRepositoryImpl
import com.tyme.github.users.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoriesModule {

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}