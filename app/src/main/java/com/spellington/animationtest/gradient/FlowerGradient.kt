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

private const val flowerGradient = """
    
    uniform shader gradient;   
    uniform shader content;   
    uniform float2 iResolution; 
    uniform float iTime;
    uniform float iRotationTimeScale;   // make if slower(smaller value) or faster.
    uniform float iInOutTimeScale;   // make if slower(smaller value) or faster.
    uniform float flowerPetals;         // bigger more spiral-ish
    uniform float iAlphaThreshold;      // transparency threshold
    uniform float2 iCenter;             // 0.5f by default
    uniform float iPetalInfluence;
    uniform float iRotationDirection;   // 1 clockwise, -1 counterclockwise
    uniform float iDirection;           // in/out
    uniform float iWobblyFactor;
    
    float2 flower(float2 uv, float spiral, float rotationTime, float inOutTime) {
        uv = 2*(uv-iCenter);
        
        if (iResolution.x < iResolution.y) {
            float aspect = iResolution.y/iResolution.x;
            uv.y *= aspect;
        } else {
            float aspect = iResolution.x/iResolution.y;
            uv.x *= aspect;
        }
  
        float pi = 3.1415926;
        float radius = length(uv);
        float angle = atan(uv.y, uv.x) + rotationTime * iRotationDirection + iWobblyFactor*sin(pi*radius+rotationTime); 
        

        float t = radius + iPetalInfluence * sin((angle + pi)*spiral) + inOutTime;
        
        return float2(t, 0);
    }
    
    half4 main(float2 fragCoord) {	    
        half4 contentColor = content.eval(fragCoord);
        float2 uv = fragCoord / iResolution;
        
        uv = flower(
            uv, 
            flowerPetals, 
            iTime*iRotationTimeScale*iRotationDirection,
            iTime*iInOutTimeScale*iDirection
        );
        
        half4 color = gradient.eval(uv);
        if (contentColor.a < iAlphaThreshold) {
            color = half4(0.);
        }
       
        return color;
    }
"""

enum class RotationDirection {
    Clockwise, CounterClockwise,
}

fun Modifier.flowerGradient(
    colors: GradientColors = GradientColors(listOf(Color.Red, Color.Blue)),
    flowerPetals: Float = 5f,
    animate: Boolean = false,
    rotationTimeScale: Float = .1f,
    inOutTimeScale: Float = .1f,
    center: Offset = Offset(.5f, .5f),
    alphaThreshold: Float = .02f,
    petalInfluence: Float = 1f,
    wobblyFactor: Float = 0f,
    direction: SpiralGradientDirection = SpiralGradientDirection.Out,
    rotationDirection: RotationDirection = RotationDirection.Clockwise,
): Modifier = composed {
    var timeMs by remember { mutableFloatStateOf(0f) }
    val runtimeShader = remember { RuntimeShader(flowerGradient) }

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
                    Shader.TileMode.MIRROR
                )
            )
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
                "flowerPetals",
                flowerPetals
            )
            runtimeShader.setFloatUniform(
                "iRotationTimeScale",
                rotationTimeScale
            )
            runtimeShader.setFloatUniform(
                "iInOutTimeScale",
                inOutTimeScale
            )
            runtimeShader.setFloatUniform(
                "iCenter",
                center.x, center.y
            )
            runtimeShader.setFloatUniform(
                "iAlphaThreshold",
                alphaThreshold,
            )
            runtimeShader.setFloatUniform(
                "iPetalInfluence",
                petalInfluence,
            )
            runtimeShader.setFloatUniform(
                "iDirection",
                if (direction == SpiralGradientDirection.Out) -1f else 1f
            )
            runtimeShader.setFloatUniform(
                "iRotationDirection",
                if (rotationDirection == RotationDirection.Clockwise) -1f else 1f
            )
            runtimeShader.setFloatUniform(
                "iWobblyFactor",
                wobblyFactor
            )
        }
}