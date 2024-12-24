package com.tyme.github.users.providers.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class DispatchersProviderImpl @Inject constructor() : DispatchersProvider {
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
}