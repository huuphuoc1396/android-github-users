package com.example.github.users.core.security.di.module

import com.example.github.users.core.security.AppSecrets
import com.example.github.users.core.security.AppSecretsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface SecurityModule {

    @Binds
    fun bindAppSecrets(impl: AppSecretsImpl): AppSecrets
}
