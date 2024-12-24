package com.tyme.github.users.ui.uistate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.tyme.github.users.ui.components.ErrorDialog
import com.tyme.github.users.ui.components.FadingVisibility
import com.tyme.github.users.ui.components.Loading
import com.tyme.github.users.ui.uistate.mappers.toString
import com.tyme.github.users.ui.uistate.viewmodel.UiStateViewModel

/**
 * A composable function that displays a UI state screen with loading and error handling.
 *
 * This composable listens to a [UiStateViewModel]'s state, handles loading and error states,
 * and emits events to the provided `onEvent` callback. The content of the screen is displayed
 * using the `content` composable lambda, which takes the current [UiState] as input.
 *
 * @param viewModel The [UiStateViewModel] to observe for UI state, error, and loading status.
 * @param lifecycle The [Lifecycle] of the composable, used to manage the collection of events.
 *        Defaults to the current lifecycle of the `LocalLifecycleOwner`.
 * @param lifecycleState The [Lifecycle.State] that controls the minimum active state of the lifecycle.
 *        Defaults to [Lifecycle.State.RESUMED].
 * @param onEvent A lambda function that receives emitted [Event] from the view model and processes it.
 * @param content A composable function that takes the [UiState] and displays the content of the screen.
 *
 * **Usage Example:**
 * ```kotlin
 * UiStateScreen(
 *     viewModel = viewModel,
 *     onEvent = { event -> /* handle event */ },
 *     content = { uiState -> Text(text = uiState.someText) }
 * )
 * ```
 */
@Composable
internal fun <UiState, Event> UiStateScreen(
    viewModel: UiStateViewModel<UiState, Event>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    onEvent: (Event) -> Unit = {},
    content: @Composable (uiState: UiState) -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        content(uiState)
        FadingVisibility(isLoading) {
            Loading()
        }
        if (error.hasError()) {
            ErrorDialog(
                message = error.toString(context),
                onDismiss = viewModel::hideError,
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.singleEvent.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = lifecycleState,
        ).collect { event ->
            onEvent(event)
        }
    }
}