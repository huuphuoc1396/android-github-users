package com.example.github.users.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.github.users.core.ui.theme.Theme

@Composable
fun UserCard(
    item: UserListItem,
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { onUserClick() }
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserAvatar(
                avatarUrl = item.avatarUrl,
                modifier = Modifier.size(90.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = item.username,
                    style = MaterialTheme.typography.titleMedium,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LinkText(
                    url = item.url,
                    onClick = onUrlClick,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    tint = if (item.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserCardFavoritePreview() {
    Theme {
        UserCard(
            item = UserListItem(
                username = "JohnDoe",
                avatarUrl = "",
                url = "https://github.com/johndoe",
                isFavorite = true,
            ),
        )
    }
}

@Preview
@Composable
private fun UserCardNotFavoritePreview() {
    Theme {
        UserCard(
            item = UserListItem(
                username = "JohnDoe",
                avatarUrl = "",
                url = "https://github.com/johndoe",
                isFavorite = false,
            ),
        )
    }
}
