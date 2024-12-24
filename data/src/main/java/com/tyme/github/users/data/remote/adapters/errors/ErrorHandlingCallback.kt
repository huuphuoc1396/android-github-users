package com.tyme.github.users.data.remote.adapters.errors

import com.tyme.github.users.data.remote.adapters.mappers.toThrowable
import com.tyme.github.users.domain.models.errors.NoConnectionException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.UnknownHostException

/**
 * A custom [Callback] implementation for handling API response errors.
 *
 * This callback checks the response status and either forwards the response to the delegate if
 * successful, or converts error responses into a [Throwable] and invokes the `onFailure` method.
 * It also handles network-related errors like `UnknownHostException` and `InterruptedIOException`
 * by returning a [NoConnectionException].
 *
 * @param delegate The original [Callback] to forward successful responses or errors to.
 *
 * **Usage Example:**
 * ```kotlin
 * val call = apiService.getData()
 * call.enqueue(ErrorHandlingCallback(callback))
 * ```
 */
internal class ErrorHandlingCallback<R>(
    private val delegate: Callback<R>,
) : Callback<R> {
    override fun onResponse(call: Call<R>, response: Response<R>) {
        if (response.isSuccessful) {
            delegate.onResponse(call, response)
        } else {
            delegate.onFailure(call, response.toThrowable())
        }
    }

    override fun onFailure(call: Call<R>, throwable: Throwable) {
        when (throwable) {
            is UnknownHostException,
            is InterruptedIOException -> delegate.onFailure(call, NoConnectionException())

            else -> delegate.onFailure(call, throwable)
        }
    }
}