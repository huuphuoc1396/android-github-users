package com.tyme.github.users.feature.users.presentation.userdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.tyme.github.users.core.ui.theme.Theme
import com.tyme.github.users.feature.users.presentation.userdetails.UserDetailUiState

@Composable
internal fun UserDetails(
    uiState: UserDetailUiState.Success,
    onBlogClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    val useHorizontalLayout = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (useHorizontalLayout) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserDetailsCard(
                    username = uiState.username,
                    avatarUrl = uiState.avatarUrl,
                    country = uiState.country,
                    modifier = Modifier.weight(1f),
                )
                UserDetailsStats(
                    followers = uiState.followers,
                    following = uiState.following,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            UserDetailsCard(
                username = uiState.username,
                avatarUrl = uiState.avatarUrl,
                country = uiState.country,
            )
            UserDetailsStats(
                followers = uiState.followers,
                following = uiState.following,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
            )
        }
        UserBlog(
            url = uiState.url,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = onBlogClick,
        )
    }
}

@Preview
@Composable
private fun UserDetailsPreview() {
    Theme {
        UserDetails(
            uiState = UserDetailUiState.Success(
                username = "user1",
                avatarUrl = "https://avatars.githubusercontent.com/u/123456",
                country = "Vietnam",
                followers = "100+",
                following = "200+",
                url = "https://tyme.com",
            ),
            onBlogClick = {},
        )
    }
}