package com.tyme.github.users.core.network.config

interface NetworkConfig {
    val baseUrl: String
    val baseDomain: String
    val pinningPublicKey: String
    val isDebug: Boolean
}
