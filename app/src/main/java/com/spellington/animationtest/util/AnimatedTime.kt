package com.spellington.animationtest.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos


/**
 * A Composable that provides a continuously updating time value that can be paused and resumed.
 *
 * This component runs a LaunchedEffect that updates a `time` state on every frame
 * only when `isPaused` is false. This causes its content to recompose and animate.
 *
 * @param isPaused A boolean state that controls the animation. If true, time does not advance.
 * @param content A lambda that receives the current time as a Float and defines the
 *                composable content to be animated.
 */
@Composable
fun PausableAnimatedTime(
    isPaused: Boolean,
    content: @Composable (time: Float) -> Unit
) {

    var accumulatedTime by remember { mutableFloatStateOf(0f) }
    var lastFrameTimeNanos by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isPaused) {
        if (!isPaused) {
            while (true) {
                val frameTimeNanos = withFrameNanos { it }

                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                }

                val deltaTime = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f

                accumulatedTime += deltaTime

                lastFrameTimeNanos = frameTimeNanos
            }
        }
    }

    content(accumulatedTime)
}
