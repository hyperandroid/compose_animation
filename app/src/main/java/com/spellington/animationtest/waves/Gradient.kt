package com.spellington.animationtest.waves

import android.graphics.LinearGradient
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.android.awaitFrame

@Immutable
@JvmInline
value class GradientColors(val colors: List<Color>)

private const val spiralGradient = """
    
    uniform shader gradient;   
    uniform shader content;   
    uniform float2 iResolution; 
    uniform float iTime;
    uniform float iTimeScale;
    uniform float iDirection;   // -1 out, 1 in.
    uniform float iSpiralThreshold; // bigger more spiral-ish
    
    float remap(float v0, float v1, float r0, float r1, float v) {
        return r0 + (r1-r0)*(v-v0)/(v1-v0);
    }
    
    float2 spiral(float2 uv, float spiral, float time) {
        float pi = 3.1415926;
        uv = (2*uv) - 1.;
        float aspect = min(iResolution.x/iResolution.y, iResolution.y/iResolution.x);
        uv.y *= aspect;
  
        float radius = length(uv);
        float angle = atan(uv.y, uv.x);
        float t = remap(-pi, pi, 0, 1, angle);
        
        t = (t + spiral*radius + time);
        
        return float2(t, 0);
    }
    
    half4 main(float2 fragCoord) {	    
        half4 contentColor = content.eval(fragCoord);
        float2 uv = fragCoord / iResolution;
        uv = spiral(uv, iSpiralThreshold, iTime*iDirection*iTimeScale);
        
        half4 color = gradient.eval(uv);
        if (contentColor.a < .02) {
            color = half4(0.);
        }
       
        return color;
    }
"""

internal fun convertComposeColors(colors: GradientColors) = colors.colors.map { color ->
    color.toArgb()
}.toIntArray()

internal fun linearGradient(colors: GradientColors) = LinearGradient(
    0f, 0f, 1f, 0f,
    convertComposeColors(colors),
    null,
    Shader.TileMode.REPEAT
)

enum class SpiralGradientDirection {
    In, Out,
}

fun Modifier.spiralGradient(
    colors: GradientColors = GradientColors(listOf(Color.Red, Color.Blue)),
    direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    spiralThreshold: Float = 5f,
    animate: Boolean = false,
    timeScale: Float = .1f,
    wavy: Boolean = false,
): Modifier = composed {
    var timeMs by remember { mutableFloatStateOf(0f) }
    val runtimeShader = remember { RuntimeShader(spiralGradient) }

    if (animate) {
        // Animate the time uniform on every frame.
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
            runtimeShader.setFloatUniform("iResolution", width, height)
            runtimeShader.setInputShader("gradient", linearGradient(colors))
        }
        .graphicsLayer {
            // This is the key: update the time uniform just before the layer is drawn.
            runtimeShader.setFloatUniform("iTime", timeMs)

            // Apply the shader as a RenderEffect.
            // 'content' in AGSL is now the composable's content.
            clip = true
            renderEffect = RenderEffect
                .createRuntimeShaderEffect(runtimeShader, "content")
                .asComposeRenderEffect()

            runtimeShader.setFloatUniform(
                "iDirection",
                if (direction == SpiralGradientDirection.Out) -1f else 1f
            )
            runtimeShader.setFloatUniform(
                "iSpiralThreshold",
                spiralThreshold
            )
            runtimeShader.setFloatUniform(
                "iTimeScale",
                timeScale
            )
        }
}