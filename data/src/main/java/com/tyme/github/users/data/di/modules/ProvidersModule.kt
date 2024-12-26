package com.tyme.github.users.data.di.modules

import com.tyme.github.users.data.providers.CertificatePinnersProvider
import com.tyme.github.users.data.providers.CertificatePinnersProviderImpl
import com.tyme.github.users.data.providers.OkHttpClientsProvider
import com.tyme.github.users.data.providers.OkHttpClientsProviderImpl
import com.tyme.github.users.data.providers.RetrofitsProvider
import com.tyme.github.users.data.providers.RetrofitsProviderImpl
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
    fun bindCertificatePinnersProvider(impl: CertificatePinnersProviderImpl): CertificatePinnersProvider

    @Binds
    fun bindOkHttpClientsProvider(impl: OkHttpClientsProviderImpl): OkHttpClientsProvider

    @Binds
    fun bindRetrofitsProvider(impl: RetrofitsProviderImpl): RetrofitsProvider
}