package com.tyme.github.users.core.common.models.errors

import androidx.annotation.StringRes

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(@StringRes val resId: Int) : UiText
}
