package com.tyme.github.users.core.ui.extensions

import com.tyme.github.users.core.common.models.errors.ApiException
import com.tyme.github.users.core.common.models.errors.NoConnectionException
import com.tyme.github.users.core.common.models.UiText
import com.tyme.github.users.core.common.models.errors.UnauthorizedException
import com.tyme.github.users.core.ui.R

fun Throwable.toUiText(): UiText = when (this) {
    is ApiException -> UiText.Dynamic(message)
    is NoConnectionException -> UiText.Resource(R.string.error_message_no_internet_connection)
    is UnauthorizedException -> UiText.Resource(R.string.error_message_unauthorized)
    else -> UiText.Resource(R.string.error_message_generic)
}
