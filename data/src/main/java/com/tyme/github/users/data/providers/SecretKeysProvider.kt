package com.tyme.github.users.data.providers

import javax.inject.Inject

internal interface SecretKeysProvider {

    fun providePiningPublicKey(): String

    fun getDatabasePassword(): String
}

internal class SecretKeysProviderImpl @Inject constructor() : SecretKeysProvider {

    override fun providePiningPublicKey(): String {
        return getSecretKey(10001)
    }

    override fun getDatabasePassword(): String {
        return getSecretKey(10002)
    }

    companion object {
        init {
            System.loadLibrary("secret-keys-lib")
        }

        @JvmStatic
        external fun getSecretKey(id: Int): String
    }
}

