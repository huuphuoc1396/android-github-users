package com.tyme.github.users.data.providers

import com.tyme.github.users.data.remote.adapters.errors.ErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

internal interface RetrofitsProvider {

    fun provideRetrofit(basUrl: String): Retrofit
}

internal class RetrofitsProviderImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : RetrofitsProvider {

    override fun provideRetrofit(basUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(basUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ErrorHandlingCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}

