package com.tyme.github.users.ui.features.userdetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyme.github.users.ui.features.components.UserAvatar
import com.tyme.github.users.ui.theme.Theme

@Composable
internal fun UserDetailsCard(
    username: String,
    avatarUrl: String,
    country: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            UserAvatar(
                avatarUrl = avatarUrl,
                modifier = Modifier.size(128.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                UserCountry(
                    country = country,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserDetailsCardPreview() {
    Theme {
        UserDetailsCard(
            username = "user0",
            avatarUrl = "https://www.github.com/user0/avatar",
            country = "India",
        )
    }
}