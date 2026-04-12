package com.tyme.github.users.core.security

import javax.inject.Inject

interface AppSecrets {
    val pinningPublicKey: String
    val databasePassword: String
}

internal class AppSecretsImpl @Inject constructor() : AppSecrets {

    override val pinningPublicKey: String
        get() = getSecretKey(10001)

    override val databasePassword: String
        get() = getSecretKey(10002)

    companion object {
        init {
            System.loadLibrary("secret-keys-lib")
        }

        @JvmStatic
        external fun getSecretKey(id: Int): String
    }
}
