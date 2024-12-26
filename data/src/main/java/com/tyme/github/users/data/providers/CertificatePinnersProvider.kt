package com.tyme.github.users.data.providers

import okhttp3.CertificatePinner
import javax.inject.Inject

internal interface CertificatePinnersProvider {

    fun provideCertificatePinner(domain: String, publicKey: String): CertificatePinner
}

internal class CertificatePinnersProviderImpl @Inject constructor() : CertificatePinnersProvider {

    override fun provideCertificatePinner(domain: String, publicKey: String): CertificatePinner {
        return CertificatePinner.Builder()
            .add(domain, "sha256/$publicKey")
            .build()
    }
}