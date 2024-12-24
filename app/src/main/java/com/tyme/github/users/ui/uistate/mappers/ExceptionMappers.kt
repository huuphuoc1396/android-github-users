package com.tyme.github.users.ui.uistate.mappers

import android.content.Context
import com.tyme.github.users.R
import com.tyme.github.users.domain.models.errors.ApiException
import com.tyme.github.users.domain.models.errors.NoConnectionException
import com.tyme.github.users.domain.models.errors.UnauthorizedException
import com.tyme.github.users.ui.uistate.models.ErrorUiState

internal fun Throwable?.toErrorUiState(): ErrorUiState {
    return when (this) {
        is ApiException -> ErrorUiState(message = message)
        is NoConnectionException -> ErrorUiState(messageRes = R.string.error_message_no_internet_connection)
        is UnauthorizedException -> ErrorUiState(messageRes = R.string.error_message_unauthorized)
        else -> ErrorUiState(messageRes = R.string.error_message_generic)
    }
}

internal fun ErrorUiState.toString(context: Context): String =
    message.ifEmpty { context.getString(messageRes) }