package com.tyme.github.users.feature.users.presentation.mappers

import com.tyme.github.users.core.network.models.errors.ApiException
import com.tyme.github.users.core.network.models.errors.NoConnectionException
import com.tyme.github.users.core.network.models.errors.UnauthorizedException
import com.tyme.github.users.feature.users.R
import com.tyme.github.users.feature.users.presentation.userdetails.UserDetailUiState
import com.tyme.github.users.feature.users.presentation.userlist.UserListUiState

private fun Throwable.toErrorParams(): Pair<String, Int> = when (this) {
    is ApiException -> message to 0
    is NoConnectionException -> "" to R.string.error_message_no_internet_connection
    is UnauthorizedException -> "" to R.string.error_message_unauthorized
    else -> "" to R.string.error_message_generic
}

internal fun Throwable.toUserListError(): UserListUiState.Error {
    val (message, messageRes) = toErrorParams()
    return UserListUiState.Error(message = message, messageRes = messageRes)
}

internal fun Throwable.toUserDetailError(): UserDetailUiState.Error {
    val (message, messageRes) = toErrorParams()
    return UserDetailUiState.Error(message = message, messageRes = messageRes)
}
