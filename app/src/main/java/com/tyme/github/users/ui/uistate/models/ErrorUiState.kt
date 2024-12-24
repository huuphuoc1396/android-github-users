package com.tyme.github.users.ui.uistate.models

import androidx.annotation.StringRes

internal data class ErrorUiState(
    val message: String = "",
    @StringRes
    val messageRes: Int = -1,
) {
    fun hasError() = message.isNotEmpty() || messageRes != -1
}