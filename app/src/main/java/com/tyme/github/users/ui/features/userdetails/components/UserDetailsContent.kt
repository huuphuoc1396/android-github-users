package com.tyme.github.users.ui.features.userdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import com.tyme.github.users.R
import com.tyme.github.users.ui.features.components.BackButton
import com.tyme.github.users.ui.features.userdetails.models.UserDetailUiState
import com.tyme.github.users.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserDetailsContent(
    userDetails: UserDetailUiState,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onBackClick: () -> Unit = {},
    onBlogClick: (String) -> Unit = {},
) {
    val isHeightCompact = remember {
        windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.user_details_title)) },
                navigationIcon = { BackButton(onClick = onBackClick) },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isHeightCompact) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UserDetailsCard(
                        username = userDetails.username,
                        avatarUrl = userDetails.avatarUrl,
                        country = userDetails.country,
                        modifier = Modifier.weight(1f),
                    )
                    UserDetailsStats(
                        followers = userDetails.followers,
                        following = userDetails.following,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                UserDetailsCard(
                    username = userDetails.username,
                    avatarUrl = userDetails.avatarUrl,
                    country = userDetails.country,
                )
                UserDetailsStats(
                    followers = userDetails.followers,
                    following = userDetails.following,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                )
            }
            UserBlog(
                url = userDetails.url,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = onBlogClick,
            )
        }
    }
}

@Preview
@Composable
private fun UserDetailsContentPreview() {
    Theme {
        UserDetailsContent(
            userDetails = UserDetailUiState(
                username = "user1",
                avatarUrl = "https://avatars.githubusercontent.com/u/123456",
                country = "Vietnam",
                followers = "100+",
                following = "200+",
                url = "https://tyme.com",
            ),
        )
    }
}