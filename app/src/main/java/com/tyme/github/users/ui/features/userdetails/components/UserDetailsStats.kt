package com.tyme.github.users.ui.features.userdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tyme.github.users.R
import com.tyme.github.users.ui.theme.Theme

@Composable
internal fun UserDetailsStats(
    followers: String,
    following: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        UserStat(
            drawableResId = R.drawable.ic_followers,
            label = stringResource(R.string.user_details_followers),
            value = followers,
        )
        UserStat(
            drawableResId = R.drawable.ic_following,
            label = stringResource(R.string.user_details_following),
            value = following,
        )
    }
}

@Preview
@Composable
private fun UserDetailsStatsPreview() {
    Theme {
        UserDetailsStats(
            followers = "100",
            following = "200",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}