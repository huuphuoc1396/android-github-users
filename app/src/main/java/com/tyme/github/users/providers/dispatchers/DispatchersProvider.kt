package com.tyme.github.users.providers.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

internal interface DispatchersProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val immediate: CoroutineDispatcher
}