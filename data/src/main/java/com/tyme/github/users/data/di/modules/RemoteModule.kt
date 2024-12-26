package com.tyme.github.users.data.di.modules

import com.tyme.github.users.data.BuildConfig
import com.tyme.github.users.data.providers.CertificatePinnersProvider
import com.tyme.github.users.data.providers.OkHttpClientsProvider
import com.tyme.github.users.data.providers.RetrofitsProvider
import com.tyme.github.users.data.providers.SecretKeysProvider
import com.tyme.github.users.data.remote.services.UserService
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
internal class RemoteModule {

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
        secretKeysProvider: SecretKeysProvider,
        certificatePinnersProvider: CertificatePinnersProvider
    ): CertificatePinner {
        return certificatePinnersProvider.provideCertificatePinner(
            domain = BuildConfig.BASE_DOMAIN,
            publicKey = secretKeysProvider.providePiningPublicKey(),
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(okHttpClientsProvider: OkHttpClientsProvider): OkHttpClient {
        return okHttpClientsProvider.provideOkHttpClient(BuildConfig.DEBUG)
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofitsProvider: RetrofitsProvider): Retrofit {
        return retrofitsProvider.provideRetrofit(BuildConfig.BASE_URL)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}