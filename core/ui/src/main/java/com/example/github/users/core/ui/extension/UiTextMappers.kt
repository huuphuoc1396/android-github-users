package com.example.github.users.core.ui.extension

import com.example.github.users.core.common.model.error.ApiException
import com.example.github.users.core.common.model.error.NoConnectionException
import com.example.github.users.core.common.model.error.UnauthorizedException
import com.example.github.users.core.ui.R
import com.example.github.users.core.ui.model.UiText

fun Throwable.toUiText(): UiText = when (this) {
    is ApiException -> UiText.Dynamic(message)
    is NoConnectionException -> UiText.Resource(R.string.error_message_no_internet_connection)
    is UnauthorizedException -> UiText.Resource(R.string.error_message_unauthorized)
    else -> UiText.Resource(R.string.error_message_generic)
}
