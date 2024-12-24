package com.tyme.github.users.ui.features.users.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.ui.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun UserList(
    users: ImmutableList<UserModel>,
    onUserClick: (UserModel) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = users,
            key = { user -> user.id },
        ) { user ->
            UserCard(
                user = user,
                onUserClick = onUserClick,
                onUrlClick = onUrlClick,
            )
        }
    }
}

@Preview
@Composable
private fun UserListPreview() {
    Theme {
        val userList = MutableList(10) { index ->
            UserModel(
                id = index,
                username = "user$index",
                url = "https://www.github.com/user$index",
            )
        }.toImmutableList()
        UserList(userList)
    }
}