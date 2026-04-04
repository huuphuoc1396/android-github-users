package com.tyme.github.users.core.ui.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openBrowser(url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
