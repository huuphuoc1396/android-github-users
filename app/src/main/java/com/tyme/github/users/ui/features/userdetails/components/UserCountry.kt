package com.tyme.github.users.ui.features.userdetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyme.github.users.R
import com.tyme.github.users.ui.theme.Theme

@Composable
internal fun UserCountry(
    country: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_location_pin),
            contentDescription = null,
        )
        Text(
            text = country,
            modifier = Modifier.padding(start = 2.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserCountryPreview() {
    Theme {
        UserCountry(
            country = "Ho Chi Minh City, \nVietnam",
            modifier = Modifier.width(200.dp),
        )
    }
}