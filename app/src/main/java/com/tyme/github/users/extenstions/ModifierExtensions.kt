package com.tyme.github.users.extenstions

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Adds a "bouncing" clickable effect to a Composable element, providing a scale and opacity animation when pressed.
 * It supports single, double, and long clicks.
 *
 * @param enabled Determines if the clickable behavior is enabled. Defaults to `true`.
 * @param pressScaleFactor The scale factor applied to the element when pressed. Defaults to `0.97f`.
 * @param pressAlphaFactor The alpha (opacity) factor applied to the element when pressed. Defaults to `0.7f`.
 * @param onLongClick Optional callback invoked on a long click event. Defaults to `null`.
 * @param onDoubleClick Optional callback invoked on a double click event. Defaults to `null`.
 * @param onClick Mandatory callback invoked on a single click event.
 *
 * This modifier creates a smooth transition for scale and opacity when the element is pressed, giving a "bouncing" feedback effect.
 * It also integrates with [combinedClickable] to handle single, double, and long click interactions.
 *
 * **Usage Example:**
 * ```
 * Box(
 *     modifier = Modifier
 *         .size(100.dp)
 *         .bouncingClickable(
 *             pressScaleFactor = 0.95f,
 *             pressAlphaFactor = 0.8f,
 *             onClick = { println("Single click") },
 *             onDoubleClick = { println("Double click") },
 *             onLongClick = { println("Long click") }
 *         )
 *         .background(Color.Blue)
 * )
 * ```
 *
 * **How it Works:**
 * - The `Modifier.graphicsLayer` is used to apply scaling and alpha changes during a press interaction.
 * - Animations are managed with [updateTransition] for smooth visual feedback.
 * - The [combinedClickable] modifier handles the various click types and uses a [MutableInteractionSource] to track press state.
 *
 * **Notes:**
 * - If `enabled` is `false`, no interactions or animations will be performed.
 * - For performance, this modifier uses the `composed` function to optimize recomposition.
 */
@ExperimentalFoundationApi
internal fun Modifier.bouncingClickable(
    enabled: Boolean = true,
    pressScaleFactor: Float = 0.97f,
    pressAlphaFactor: Float = 0.7f,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animationTransition = updateTransition(
        targetState = isPressed,
        label = "BouncingClickableTransition",
    )
    val scaleFactor by animationTransition.animateFloat(
        targetValueByState = { pressed -> if (pressed) pressScaleFactor else 1f },
        label = "BouncingClickableScaleFactorTransition",
    )
    val opacity by animationTransition.animateFloat(
        targetValueByState = { pressed -> if (pressed) pressAlphaFactor else 1f },
        label = "BouncingClickableAlphaTransition",
    )
    this
        .graphicsLayer {
            scaleX = scaleFactor
            scaleY = scaleFactor
            alpha = opacity
        }
        .combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick,
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
        )
}
