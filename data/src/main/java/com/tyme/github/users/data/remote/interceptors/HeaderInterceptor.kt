package com.tyme.github.users.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An OkHttp interceptor that adds common headers to HTTP requests.
 *
 * This interceptor automatically adds the following headers to all outgoing requests:
 * - `Accept: application/json`
 * - `Content-Type: application/json`
 *
 * These headers indicate that the server should respond with JSON and that the body of the request is also in JSON format.
 *
 * **Usage Example:**
 * ```
 * val okHttpClient = OkHttpClient.Builder()
 *     .addInterceptor(HeaderInterceptor())
 *     .build()
 * ```
 */
internal class HeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}