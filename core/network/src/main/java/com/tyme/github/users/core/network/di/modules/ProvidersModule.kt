package com.tyme.github.users.core.network.di.modules

import com.tyme.github.users.core.network.providers.CertificatePinnersProvider
import com.tyme.github.users.core.network.providers.CertificatePinnersProviderImpl
import com.tyme.github.users.core.network.providers.OkHttpClientsProvider
import com.tyme.github.users.core.network.providers.OkHttpClientsProviderImpl
import com.tyme.github.users.core.network.providers.RetrofitsProvider
import com.tyme.github.users.core.network.providers.RetrofitsProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface ProvidersModule {

    @Binds
    fun bindCertificatePinnersProvider(impl: CertificatePinnersProviderImpl): CertificatePinnersProvider

    @Binds
    fun bindOkHttpClientsProvider(impl: OkHttpClientsProviderImpl): OkHttpClientsProvider

    @Binds
    fun bindRetrofitsProvider(impl: RetrofitsProviderImpl): RetrofitsProvider
}
