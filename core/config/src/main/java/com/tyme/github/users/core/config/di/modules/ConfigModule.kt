package com.tyme.github.users.core.config.di.modules

import com.tyme.github.users.core.config.BuildConfig
import com.tyme.github.users.core.config.NetworkConfigImpl
import com.tyme.github.users.core.network.config.NetworkConfig
import com.tyme.github.users.core.security.providers.SecretKeysProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ConfigModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(secretKeysProvider: SecretKeysProvider): NetworkConfig {
        return NetworkConfigImpl(
            baseUrl = BuildConfig.BASE_URL,
            baseDomain = BuildConfig.BASE_DOMAIN,
            pinningPublicKey = secretKeysProvider.providePiningPublicKey(),
            isDebug = BuildConfig.DEBUG,
        )
    }
}
