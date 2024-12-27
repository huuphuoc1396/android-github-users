package com.tyme.github.users.data.providers

import com.tyme.github.users.data.remote.interceptors.HeaderInterceptor
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal interface OkHttpClientsProvider {

    fun provideOkHttpClient(isEnabledLogging: Boolean): OkHttpClient
}

internal class OkHttpClientsProviderImpl @Inject constructor(
    private val headerInterceptor: HeaderInterceptor,
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val certificatePinner: CertificatePinner,
) : OkHttpClientsProvider {

    override fun provideOkHttpClient(isEnabledLogging: Boolean): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .certificatePinner(certificatePinner)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .apply {
                if (isEnabledLogging) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }
            .build()
    }

    private companion object {
        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 30L
        private const val WRITE_TIMEOUT = 30L
    }
}