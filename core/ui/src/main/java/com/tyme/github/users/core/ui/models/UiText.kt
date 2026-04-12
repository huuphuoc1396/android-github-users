package com.tyme.github.users.core.ui.models

import androidx.annotation.StringRes

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(@StringRes val resId: Int) : UiText
}