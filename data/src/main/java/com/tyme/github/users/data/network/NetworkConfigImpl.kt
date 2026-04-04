package com.tyme.github.users.data.network

import com.tyme.github.users.core.network.config.NetworkConfig
import com.tyme.github.users.data.BuildConfig
import com.tyme.github.users.data.providers.SecretKeysProvider
import javax.inject.Inject

internal class NetworkConfigImpl @Inject constructor(
    private val secretKeysProvider: SecretKeysProvider,
) : NetworkConfig {
    override val baseUrl: String = BuildConfig.BASE_URL
    override val baseDomain: String = BuildConfig.BASE_DOMAIN
    override val pinningPublicKey: String get() = secretKeysProvider.providePiningPublicKey()
    override val isDebug: Boolean = BuildConfig.DEBUG
}
