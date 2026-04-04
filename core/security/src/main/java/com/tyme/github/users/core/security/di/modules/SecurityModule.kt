package com.tyme.github.users.core.security.di.modules

import com.tyme.github.users.core.security.providers.SecretKeysProvider
import com.tyme.github.users.core.security.providers.SecretKeysProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface SecurityModule {

    @Binds
    fun bindSecretKeysProvider(impl: SecretKeysProviderImpl): SecretKeysProvider
}
