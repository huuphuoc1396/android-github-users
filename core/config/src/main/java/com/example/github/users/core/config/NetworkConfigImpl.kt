package com.example.github.users.core.config

import com.example.github.users.core.network.config.NetworkConfig

class NetworkConfigImpl(
    override val baseUrl: String,
    override val baseDomain: String,
    override val pinningPublicKey: String,
    override val isDebug: Boolean,
) : NetworkConfig
