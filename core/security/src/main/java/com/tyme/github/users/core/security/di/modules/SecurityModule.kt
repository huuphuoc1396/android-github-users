package com.tyme.github.users.core.security.di.modules

import com.tyme.github.users.core.security.AppSecrets
import com.tyme.github.users.core.security.AppSecretsImpl
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
