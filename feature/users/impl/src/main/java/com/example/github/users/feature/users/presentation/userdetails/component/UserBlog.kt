package com.example.github.users.feature.users.presentation.userdetails.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.github.users.core.ui.component.LinkText
import com.example.github.users.core.ui.theme.Theme
import com.example.github.users.feature.users.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UserBlog(
    url: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.user_details_blog),
            style = MaterialTheme.typography.titleLarge,
        )
        LinkText(
            url = url,
            onClick = onClick,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserBlogPreview() {
    Theme {
        UserBlog(
            url = "https://example.com",
        )
    }
}
