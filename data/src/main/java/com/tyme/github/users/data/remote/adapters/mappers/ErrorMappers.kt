package com.tyme.github.users.data.remote.adapters.mappers

import com.google.gson.Gson
import com.tyme.github.users.data.remote.responses.errors.ErrorResponse
import com.tyme.github.users.domain.models.errors.ApiException
import com.tyme.github.users.domain.models.errors.UnauthorizedException
import com.tyme.github.users.domain.extensions.orEmpty
import com.tyme.github.users.domain.extensions.orZero
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection

internal fun Response<*>.toThrowable(): Throwable {
    if (code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
        return UnauthorizedException()
    }
    val jsonString = errorBody()?.string()
    val errorResponse = try {
        val gson = Gson()
        gson.fromJson(jsonString, ErrorResponse::class.java)
    } catch (exception: Exception) {
        Timber.e(exception, "Error parsing: \n$jsonString")
        return exception
    }
    return ApiException(
        code = errorResponse?.code.orZero(),
        message = errorResponse?.message.orEmpty(),
    )
}