package com.tyme.github.users.ui.uistate.mappers

import android.content.Context
import com.tyme.github.users.R
import com.tyme.github.users.domain.models.errors.ApiException
import com.tyme.github.users.domain.models.errors.NoConnectionException
import com.tyme.github.users.domain.models.errors.UnauthorizedException
import com.tyme.github.users.ui.uistate.models.ErrorUiState

/**
 * Extension function to convert a [Throwable] into an [ErrorUiState].
 *
 * This function maps different types of exceptions to corresponding error UI states.
 * For example, it handles specific exceptions like [ApiException], [NoConnectionException], and [UnauthorizedException],
 * and provides appropriate error messages or string resources for display.
 *
 * @return An [ErrorUiState] representing the error details to display in the UI.
 *
 * **Usage Example:**
 * ```kotlin
 * val errorUiState = throwable.toErrorUiState()
 * ```
 */
internal fun Throwable?.toErrorUiState(): ErrorUiState {
    return when (this) {
        is ApiException -> ErrorUiState(message = message)
        is NoConnectionException -> ErrorUiState(messageRes = R.string.error_message_no_internet_connection)
        is UnauthorizedException -> ErrorUiState(messageRes = R.string.error_message_unauthorized)
        else -> ErrorUiState(messageRes = R.string.error_message_generic)
    }
}

/**
 * Extension function to convert an [ErrorUiState] to a string.
 *
 * This function retrieves the error message either from the `message` property (if it's non-empty),
 * or falls back to fetching the string from the resource ID provided in `messageRes`.
 *
 * @param context The [Context] used to retrieve the string resource if needed.
 * @return The error message as a string.
 *
 * **Usage Example:**
 * ```kotlin
 * val errorMessage = errorUiState.toString(context)
 * ```
 */
internal fun ErrorUiState.toString(context: Context): String =
    message.ifEmpty { context.getString(messageRes) }