package com.tyme.github.users.ui.uistate.models

/**
 * A singleton object representing a state with no UI updates or changes.
 *
 * This can be used in scenarios where a specific UI state is not required
 * or when an operation does not directly affect the UI.
 *
 * **Usage Example:**
 * ```
 * class MyViewModel : UiStateViewModel<NoUiState, MyEvent>(NoUiState) {
 *     fun performAction() {
 *         launchSafe {
 *             // Perform some action that doesn't change UI state
 *         }
 *     }
 * }
 * ```
 */
internal data object NoUiState