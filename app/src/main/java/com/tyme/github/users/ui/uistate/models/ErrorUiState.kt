package com.tyme.github.users.ui.uistate.models

import androidx.annotation.StringRes

/**
 * A data class representing the error state of the UI.
 *
 * @property message The error message as a [String]. Defaults to an empty string.
 * @property messageRes The resource ID for the error message as an integer. Defaults to `-1`.
 *
 * This class provides a method to check if an error is present based on the message or resource ID.
 *
 * **Usage Example:**
 * ```
 * val errorState = ErrorUiState(message = "An error occurred")
 * if (errorState.hasError()) {
 *     // Handle the error
 * }
 * ```
 */
internal data class ErrorUiState(
    val message: String = "",
    @StringRes
    val messageRes: Int = -1,
) {
    fun hasError() = message.isNotEmpty() || messageRes != -1
}