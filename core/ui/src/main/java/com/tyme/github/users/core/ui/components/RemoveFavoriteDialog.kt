package com.tyme.github.users.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyme.github.users.core.ui.R

@Composable
fun RemoveFavoriteDialog(
    username: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        shape = RoundedCornerShape(16.dp),
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { Text(text = stringResource(R.string.remove_favorite_dialog_title)) },
        text = { Text(text = stringResource(R.string.remove_favorite_dialog_message, username)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.remove_favorite_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.button_cancel))
            }
        },
    )
}

@Preview
@Composable
private fun RemoveFavoriteDialogPreview() {
    RemoveFavoriteDialog(
        username = "JohnDoe",
        onConfirm = {},
        onDismiss = {},
    )
}
