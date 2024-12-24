package com.tyme.github.users.ui.features.users.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.ui.components.LinkText
import com.tyme.github.users.ui.features.components.UserAvatar
import com.tyme.github.users.ui.theme.Theme

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UserCard(
    user: UserModel,
    modifier: Modifier = Modifier,
    onUserClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            contentColor = Black,
            containerColor = White,
        ),
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Row(
            modifier = Modifier
                .clickable { onUserClick(user) }
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            UserAvatar(
                avatarUrl = user.avatarUrl,
                modifier = Modifier.size(90.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LinkText(
                    url = user.url,
                    onClick = onUrlClick,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserCardPreview() {
    Theme {
        UserCard(
            user = UserModel(
                id = 1,
                username = "JohnDoe",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                url = "https://github.com/joindoe",
            ),
        )
    }
}