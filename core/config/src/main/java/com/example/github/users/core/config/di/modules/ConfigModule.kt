package com.example.github.users.core.config.di.modules

import com.example.github.users.core.config.BuildConfig
import com.example.github.users.core.config.NetworkConfigImpl
import com.example.github.users.core.network.config.NetworkConfig
import com.example.github.users.core.security.AppSecrets
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
    fun provideNetworkConfig(appSecrets: AppSecrets): NetworkConfig {
        return NetworkConfigImpl(
            baseUrl = BuildConfig.BASE_URL,
            baseDomain = BuildConfig.BASE_DOMAIN,
            pinningPublicKey = appSecrets.pinningPublicKey,
            isDebug = BuildConfig.DEBUG,
        )
    }
}
