package com.tyme.github.users.extenstions

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Opens the provided URL in the default web browser.
 *
 * @param url The web URL to be opened. Must be a valid URL (e.g., "https://example.com").
 *
 * This function uses an Intent with the action [Intent.ACTION_VIEW] to launch the URL.
 * Any exceptions that occur during the process (e.g., invalid URL format, no browser available)
 * are caught silently to prevent app crashes.
 *
 * Usage Example:
 * ```
 * val context: Context = this // Replace with your Context instance
 * val url = "https://example.com"
 * context.openBrowser(url)
 * ```
 */
fun Context.openBrowser(url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}