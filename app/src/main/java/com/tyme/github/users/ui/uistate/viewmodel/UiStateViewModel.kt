package com.tyme.github.users.ui.uistate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyme.github.users.extenstions.viewmodel.launch
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

internal abstract class UiStateViewModel<UiState, Event>(initialUiState: UiState) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    private val _error = MutableStateFlow(ErrorUiState())
    val error: StateFlow<ErrorUiState>
        get() = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _singleEvent = Channel<Event>(Channel.BUFFERED)
    val singleEvent: Flow<Event>
        get() = _singleEvent.receiveAsFlow()

    val uiState: UiState
        get() = _uiState.value

    open fun updateUiState(update: UiState.() -> UiState) {
        _uiState.update { it.update() }
    }

    open fun showError(throwable: Throwable) {
        _error.update { throwable.toErrorUiState() }
    }

    open fun hideError() {
        _error.update { ErrorUiState() }
    }

    open fun showLoading() {
        _isLoading.update { true }
    }

    open fun hideLoading() {
        _isLoading.update { false }
    }

    open fun sendEvent(event: Event) {
        launch { _singleEvent.send(event) }
    }

    fun launchSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        onError: (Throwable) -> Unit = {},
        hasLoading: Boolean = false,
        block: suspend () -> Unit,
    ): Job {
        if (hasLoading) showLoading()
        return launch(context) {
            try {
                block()
            } catch (e: Throwable) {
                Timber.e(e)
                onError(e)
            }
            if (hasLoading) hideLoading()
        }
    }

    fun <T> Flow<T>.collectSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        hasLoading: Boolean = false,
        onError: (Throwable) -> Unit = {},
        block: suspend (T) -> Unit,
    ): Job = flowOn(context)
        .onStart { if (hasLoading) showLoading() }
        .catch { e ->
            Timber.e(e)
            onError(e)
            if (hasLoading) hideLoading()
        }
        .onEach {
            block(it)
            if (hasLoading) hideLoading()
        }
        .launchIn(viewModelScope)
}