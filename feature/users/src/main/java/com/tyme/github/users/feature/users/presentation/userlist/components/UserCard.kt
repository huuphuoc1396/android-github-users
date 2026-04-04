package com.tyme.github.users.feature.users.presentation.userlist.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.users.domain.model.UserModel

@Composable
internal fun UserCard(
    user: UserModel,
    modifier: Modifier = Modifier,
    onUserClick: (UserModel) -> Unit = {},
    onFavoriteClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
) {
    com.tyme.github.users.core.ui.components.UserCard(
        username = user.username,
        avatarUrl = user.avatarUrl,
        url = user.url,
        isFavorite = user.isFavorite,
        modifier = modifier,
        onUserClick = { onUserClick(user) },
        onFavoriteClick = { onFavoriteClick(user) },
        onUrlClick = onUrlClick,
    )
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
                isFavorite = false,
            ),
        )
    }
}
