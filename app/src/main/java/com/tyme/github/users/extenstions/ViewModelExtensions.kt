package com.tyme.github.users.extenstions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launches a coroutine within the `viewModelScope` of a `ViewModel`.
 *
 * @param context An optional [CoroutineContext] to override the default coroutine context.
 *                Defaults to [EmptyCoroutineContext].
 * @param start A [CoroutineStart] value to control the start behavior of the coroutine.
 *              Defaults to [CoroutineStart.DEFAULT].
 * @param block A suspending block of code to execute within the coroutine scope.
 *
 * @return A [Job] representing the coroutine. The returned job can be used to manage or observe
 *         the lifecycle of the coroutine.
 *
 * **Usage Example:**
 * ```
 * class MyViewModel : ViewModel() {
 *     fun fetchData() {
 *         launch {
 *             val result = repository.getData()
 *             _uiState.value = result
 *         }
 *     }
 * }
 * ```
 *
 * **How it Works:**
 * - This function is a utility extension for the `ViewModel` class, simplifying the usage of coroutines within a `ViewModel`.
 * - It delegates the coroutine launch to the `viewModelScope`, which ensures that all coroutines are canceled when the `ViewModel` is cleared.
 * - You can optionally specify a custom `CoroutineContext` or `CoroutineStart` mode for more advanced use cases.
 *
 * **Notes:**
 * - Ensure the `block` provided is safe for execution on the main thread, as `viewModelScope` operates with `Dispatchers.Main` by default.
 * - For long-running or intensive operations, consider using `Dispatchers.IO` in the `context` parameter.
 */
internal fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return viewModelScope.launch(
        context = context,
        start = start,
        block = block,
    )
}