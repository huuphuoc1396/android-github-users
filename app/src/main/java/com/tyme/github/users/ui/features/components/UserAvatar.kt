package com.tyme.github.users.ui.features.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tyme.github.users.R
import com.tyme.github.users.ui.theme.Theme
import com.tyme.github.users.ui.utils.forwardingPainter

@Composable
internal fun UserAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier,
) {
    val colors = CardDefaults.cardColors(
        contentColor = MaterialTheme.colorScheme.tertiary,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
    )
    Card(
        modifier = modifier,
        colors = colors,
    ) {
        val placeholder = forwardingPainter(
            painter = painterResource(R.drawable.ic_placeholder),
            colorFilter = ColorFilter.tint(colors.contentColor)
        )
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder,
        )
    }
}

@Preview
@Composable
private fun UserAvatarPreview() {
    Theme {
        UserAvatar(
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            modifier = Modifier.size(120.dp),
        )
    }
}