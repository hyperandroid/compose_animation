package com.spellington.animationtest.gradient

import android.graphics.LinearGradient
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.android.awaitFrame

private const val wavyGradient = """
    
"""

fun Modifier.wavyGradient(
    colors: GradientColors = GradientColors(listOf(Color.Red, Color.Blue)),
    animate: Boolean = false,
    timeScale: Float = .1f,
    center: Offset = Offset(.5f, .5f),
    alphaThreshold: Float = .02f,
    tileMode: Shader.TileMode = Shader.TileMode.CLAMP,
): Modifier = composed {
    var timeMs by remember { mutableFloatStateOf(0f) }
    val runtimeShader = remember { RuntimeShader(wavyGradient) }

    if (animate) {
        LaunchedEffect(Unit) {
            var startNanos = 0L
            while (true) {
                val frameTimeNanos = awaitFrame()
                if (startNanos == 0L) {
                    startNanos = frameTimeNanos
                }
                timeMs = (frameTimeNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    this
        .onSizeChanged { size ->
            val width = size.width.toFloat()
            val height = size.height.toFloat()
            runtimeShader.setFloatUniform(
                "iResolution",
                width, height
            )
            runtimeShader.setInputShader(
                "gradient",
                LinearGradient(
                    0f, 0f, 1f, 0f,
                    colors.colors.map { it.toArgb() }.toIntArray(),
                    null,
                    tileMode,
                )
            )
        }
        .graphicsLayer {

            clip = true

            renderEffect = RenderEffect
                .createRuntimeShaderEffect(runtimeShader, "content")
                .asComposeRenderEffect()

            runtimeShader.setFloatUniform(
                "iTime",
                timeMs
            )
            runtimeShader.setFloatUniform(
                "iTimeScale",
                timeScale
            )
            runtimeShader.setFloatUniform(
                "iCenter",
                center.x, center.y
            )
            runtimeShader.setFloatUniform(
                "iAlphaThreshold",
                alphaThreshold,
            )
        }
}