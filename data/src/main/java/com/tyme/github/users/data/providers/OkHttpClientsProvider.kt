package com.tyme.github.users.data.providers

import com.tyme.github.users.data.remote.interceptors.HeaderInterceptor
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            .apply {
                if (isEnabledLogging) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }
            .build()
    }
}