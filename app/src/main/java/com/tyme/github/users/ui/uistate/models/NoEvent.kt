package com.tyme.github.users.ui.uistate.models

/**
 * A singleton object representing the absence of events.
 *
 * This can be used in scenarios where no specific events need to be emitted or handled.
 *
 * **Usage Example:**
 * ```
 * class MyViewModel : UiStateViewModel<MyUiState, NoEvent>(MyUiState()) {
 *     fun performAction() {
 *         launchSafe {
 *             // Perform some action without emitting events
 *         }
 *     }
 * }
 * ```
 */
internal data object NoEvent