package com.example.github.users.core.ui.extension

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openBrowser(url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}
