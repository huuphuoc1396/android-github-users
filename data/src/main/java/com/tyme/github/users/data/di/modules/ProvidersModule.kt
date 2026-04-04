package com.tyme.github.users.data.di.modules

import com.tyme.github.users.core.network.config.NetworkConfig
import com.tyme.github.users.data.network.NetworkConfigImpl
import com.tyme.github.users.data.providers.SecretKeysProvider
import com.tyme.github.users.data.providers.SecretKeysProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface ProvidersModule {

    @Binds
    fun bindKeysProvider(impl: SecretKeysProviderImpl): SecretKeysProvider

    @Binds
    fun bindNetworkConfig(impl: NetworkConfigImpl): NetworkConfig
}