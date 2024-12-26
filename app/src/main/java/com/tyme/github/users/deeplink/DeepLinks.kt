package com.tyme.github.users.deeplink

internal object DeepLinks {

    private const val HOST = "https://github-users.tyme.com"

    private const val USER_LIST = "users"

    private const val USER_DETAILS = "users/{username}"

    const val USER_LIST_PATH = "$HOST/$USER_LIST"

    const val USER_DETAILS_PATH = "$HOST/$USER_DETAILS"
}