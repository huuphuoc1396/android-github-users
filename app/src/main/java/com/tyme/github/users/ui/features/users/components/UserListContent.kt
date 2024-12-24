package com.tyme.github.users.ui.features.users.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tyme.github.users.ui.features.users.models.UserListUiState
import com.tyme.github.users.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserListContent(
    userList: UserListUiState,
    modifier: Modifier = Modifier,
    onUserClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("GitHub Users") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}

@Preview
@Composable
private fun UserListContentPreview() {
    Theme {
        UserListContent(
            userList = UserListUiState,
        )
    }
}