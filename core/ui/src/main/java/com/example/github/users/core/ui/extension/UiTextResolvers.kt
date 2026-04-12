package com.example.github.users.core.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.github.users.core.ui.model.UiText

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> stringResource(resId)
}
