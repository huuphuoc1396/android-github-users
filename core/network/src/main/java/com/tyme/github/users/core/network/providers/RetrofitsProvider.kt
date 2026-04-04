package com.tyme.github.users.core.network.providers

import com.tyme.github.users.core.network.adapters.errors.ErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

internal interface RetrofitsProvider {

    fun provideRetrofit(baseUrl: String): Retrofit
}

internal class RetrofitsProviderImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : RetrofitsProvider {

    override fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}
