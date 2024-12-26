package com.tyme.github.users.providers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Interface that provides access to various [CoroutineDispatcher]s for different threads.
 *
 * This interface is used to abstract out the dispatcher dependencies in the application,
 * allowing for easy swapping of dispatchers for unit testing or other purposes.
 * It defines the main dispatchers for different use cases:
 * - **default**: Used for CPU-intensive work.
 * - **main**: Used for UI-related work on the main thread.
 * - **io**: Used for I/O-bound operations (e.g., network or disk operations).
 * - **immediate**: Used for immediate execution, bypassing any dispatcher queueing if possible.
 *
 * **Usage Example:**
 * ```kotlin
 * class MyViewModel(private val dispatchersProvider: DispatchersProvider) : ViewModel() {
 *     fun fetchData() {
 *         viewModelScope.launch(dispatchersProvider.io) {
 *             // Perform I/O work
 *         }
 *     }
 * }
 * ```
 */
internal interface DispatchersProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val immediate: CoroutineDispatcher
}

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