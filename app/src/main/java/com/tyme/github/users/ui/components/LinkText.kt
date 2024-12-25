package com.tyme.github.users.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.tyme.github.users.extenstions.bouncingClickable
import com.tyme.github.users.ui.theme.Theme

@ExperimentalFoundationApi
@Composable
internal fun LinkText(
    url: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        text = url,
        modifier = modifier.bouncingClickable { onClick(url) },
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
        style = style,
    )
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun LinkTextPreview() {
    Theme {
        LinkText(
            url = "https://www.github.com",
            onClick = {},
        )
    }
}