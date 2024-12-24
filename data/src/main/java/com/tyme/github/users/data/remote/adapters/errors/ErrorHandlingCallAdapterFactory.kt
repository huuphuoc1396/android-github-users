package com.tyme.github.users.data.remote.adapters.errors

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A custom [CallAdapter.Factory] that provides an [ErrorHandlingCallAdapter] for Retrofit calls.
 *
 * This factory is responsible for creating instances of [ErrorHandlingCallAdapter], which intercept
 * Retrofit API calls and handle errors by using custom callbacks.
 *
 * @see ErrorHandlingCallAdapter
 *
 * **Usage Example:**
 * ```kotlin
 * val retrofit = Retrofit.Builder()
 *     .addCallAdapterFactory(ErrorHandlingCallAdapterFactory.create())
 *     .build()
 * ```
 */
internal class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *> {
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        return ErrorHandlingCallAdapter<Any>(responseType)
    }

    companion object {
        fun create() = ErrorHandlingCallAdapterFactory()
    }
}


