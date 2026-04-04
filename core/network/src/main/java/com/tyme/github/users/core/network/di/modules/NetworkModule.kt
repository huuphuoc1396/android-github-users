package com.tyme.github.users.core.network.di.modules

import com.tyme.github.users.core.network.config.NetworkConfig
import com.tyme.github.users.core.network.providers.CertificatePinnersProvider
import com.tyme.github.users.core.network.providers.OkHttpClientsProvider
import com.tyme.github.users.core.network.providers.RetrofitsProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideCertificatePinner(
        networkConfig: NetworkConfig,
        certificatePinnersProvider: CertificatePinnersProvider,
    ): CertificatePinner {
        return certificatePinnersProvider.provideCertificatePinner(
            domain = networkConfig.baseDomain,
            publicKey = networkConfig.pinningPublicKey,
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkConfig: NetworkConfig,
        okHttpClientsProvider: OkHttpClientsProvider,
    ): OkHttpClient {
        return okHttpClientsProvider.provideOkHttpClient(networkConfig.isDebug)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        networkConfig: NetworkConfig,
        retrofitsProvider: RetrofitsProvider,
    ): Retrofit {
        return retrofitsProvider.provideRetrofit(networkConfig.baseUrl)
    }
}
