package com.tyme.github.users.ui.uistate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyme.github.users.extenstions.launch
import com.tyme.github.users.ui.uistate.mappers.toErrorUiState
import com.tyme.github.users.ui.uistate.models.ErrorUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Base ViewModel class to manage UI state, loading state, error state, and single events.
 *
 * @param UiState The type representing the UI state.
 * @param Event The type representing single, one-time events.
 * @param initialUiState The initial state of the UI.
 *
 * This class provides utilities for managing:
 * - UI state updates.
 * - Error state handling.
 * - Loading state indication.
 * - Emission of single-use events.
 *
 * **Usage Example:**
 * ```
 * data class MyUiState(val data: String = "")
 * sealed class MyEvent {
 *     object ShowToast : MyEvent()
 * }
 *
 * class MyViewModel : UiStateViewModel<MyUiState, MyEvent>(MyUiState()) {
 *     fun loadData() {
 *         launchSafe(hasLoading = true) {
 *             val result = repository.getData()
 *             updateUiState { copy(data = result) }
 *         }
 *     }
 * }
 * ```
 */
internal abstract class UiStateViewModel<UiState, Event>(initialUiState: UiState) : ViewModel() {

    // Backing property for UI state
    private val _uiState = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    // Backing property for error state
    private val _error = MutableStateFlow(ErrorUiState())
    val error: StateFlow<ErrorUiState>
        get() = _error.asStateFlow()

    // Backing property for loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    // Backing property for single-use events
    private val _singleEvent = Channel<Event>(Channel.BUFFERED)
    val singleEvent: Flow<Event>
        get() = _singleEvent.receiveAsFlow()

    // Accessor for the current UI state value
    val uiState: UiState
        get() = _uiState.value

    /**
     * Updates the current UI state using the provided update function.
     *
     * @param update A function that applies the desired changes to the current UI state.
     */
    open fun updateUiState(update: UiState.() -> UiState) {
        _uiState.update { it.update() }
    }

    /**
     * Updates the error state to display an error.
     *
     * @param throwable The throwable to be transformed into an [ErrorUiState].
     */
    open fun showError(throwable: Throwable) {
        _error.update { throwable.toErrorUiState() }
    }

    /**
     * Resets the error state to its initial value.
     */
    open fun hideError() {
        _error.update { ErrorUiState() }
    }

    /**
     * Shows the loading state.
     *
     * @param isLoading If `true`, shows the loading state.
     */
    open fun setLoading(isLoading: Boolean) {
        _isLoading.update { isLoading }
    }

    /**
     * Sends a one-time event.
     *
     * @param event The event to be sent.
     */
    open fun sendEvent(event: Event) {
        launch { _singleEvent.send(event) }
    }

    /**
     * Launches a coroutine with error handling and optional loading state management.
     *
     * @param context The coroutine context to use. Defaults to [EmptyCoroutineContext].
     * @param onError A callback invoked when an error occurs.
     * @param hasLoading If `true`, updates the loading state during execution. Defaults to `false`.
     * @param block The suspending code block to execute.
     * @return A [Job] representing the launched coroutine.
     */
    fun launchSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        onError: (Throwable) -> Unit = {},
        hasLoading: Boolean = false,
        block: suspend () -> Unit,
    ): Job {
        if (hasLoading) setLoading(true)
        return launch(context) {
            try {
                block()
            } catch (e: Throwable) {
                Timber.e(e)
                onError(e)
            }
            if (hasLoading) setLoading(false)
        }
    }

    /**
     * Collects a [Flow] with error handling and optional loading state management.
     *
     * @param context The coroutine context to use. Defaults to [EmptyCoroutineContext].
     * @param hasLoading If `true`, updates the loading state during execution. Defaults to `false`.
     * @param onError A callback invoked when an error occurs.
     * @param block A lambda executed for each emitted value of the flow.
     * @return A [Job] representing the launched coroutine.
     */
    fun <T> Flow<T>.collectSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        hasLoading: Boolean = false,
        onError: (Throwable) -> Unit = {},
        block: suspend (T) -> Unit,
    ): Job = flowOn(context)
        .onStart { if (hasLoading) setLoading(true) }
        .catch { e ->
            Timber.e(e)
            onError(e)
            if (hasLoading) setLoading(false)
        }
        .onEach {
            block(it)
            if (hasLoading) setLoading(false)
        }
        .launchIn(viewModelScope)
}