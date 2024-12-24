package com.tyme.github.users.ui.features.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tyme.github.users.R
import com.tyme.github.users.ui.theme.Theme

@Composable
internal fun UserAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.ic_placeholder),
            placeholder = painterResource(R.drawable.ic_placeholder),
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