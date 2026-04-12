package com.tyme.github.users.core.network.di.modules

import com.tyme.github.users.core.network.adapters.errors.ErrorHandlingCallAdapterFactory
import com.tyme.github.users.core.network.config.NetworkConfig
import com.tyme.github.users.core.network.interceptors.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideCertificatePinner(config: NetworkConfig): CertificatePinner =
        CertificatePinner.Builder()
            .add(config.baseDomain, "sha256/${config.pinningPublicKey}")
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        config: NetworkConfig,
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        certificatePinner: CertificatePinner,
    ): OkHttpClient = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .apply {
            interceptors.forEach { addInterceptor(it) }
            if (config.isDebug) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(config: NetworkConfig, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory.create())
            .client(client)
            .build()

    @Provides
    @IntoSet
    @Singleton
    fun provideHeaderInterceptor(): Interceptor = HeaderInterceptor()

    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
}