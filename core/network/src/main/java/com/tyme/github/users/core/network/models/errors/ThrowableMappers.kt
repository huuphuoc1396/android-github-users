package com.tyme.github.users.core.network.models.errors

import androidx.annotation.StringRes
import com.tyme.github.users.core.network.R

data class NetworkErrorMessage(
    val message: String = "",
    @param:StringRes val messageRes: Int = 0,
)

fun Throwable.toNetworkErrorMessage(): NetworkErrorMessage = when (this) {
    is ApiException -> NetworkErrorMessage(message = message)
    is NoConnectionException -> NetworkErrorMessage(messageRes = R.string.error_message_no_internet_connection)
    is UnauthorizedException -> NetworkErrorMessage(messageRes = R.string.error_message_unauthorized)
    else -> NetworkErrorMessage(messageRes = R.string.error_message_generic)
}
