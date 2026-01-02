package com.spellington.animationtest.gradient

import android.graphics.LinearGradient
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb

@Immutable
@JvmInline
value class GradientColors(val colors: List<Color>)

enum class SpiralGradientDirection {
    In, Out;

    fun toFloat() = if (this == Out)
        -1f
    else
        1f
}

class SpiralShader() {

    var shader: RuntimeShader = RuntimeShader(SPIRAL_GRADIENT_SHADER)
        private set

    private var _resolution: Size = Size.Zero
    var resolution: Size
        get() = _resolution
        set(value) {
            if (_resolution == value) return
            _resolution = value
            shader.setFloatUniform("iResolution", _resolution.width, _resolution.height)
        }

    private var _direction = SpiralGradientDirection.Out
    var  direction: SpiralGradientDirection
        get() = _direction
        set(value) {

            if (_direction == value) return
            _direction = value
            shader.setFloatUniform("iDirection", _direction.toFloat())
        }

    private var _spiralThreshold = 5f
    var spiralThreshold: Float
        get() = _spiralThreshold
        set(value) {
            if (_spiralThreshold == value) return
            _spiralThreshold = value
            shader.setFloatUniform("iSpiralThreshold", _spiralThreshold)
        }

    private var _timeScale = .1f
    var timeScale: Float
        get() = _timeScale
        set(value) {
            if (_timeScale == value) return
            _timeScale = value
            shader.setFloatUniform("iTimeScale", _timeScale)
        }

    private var _center: Offset = Offset(.5f, .5f)
    var center: Offset
        get() = _center
        set(value) {
            if (_center ==value) return
            _center = value
            shader.setFloatUniform("iCenter", _center.x, _center.y)
        }

    private var _time: Float = 0f
    var time: Float
        get() = _time
        set(value) {
            if (_time == value) return
            _time = value
            shader.setFloatUniform("iTime", value)
        }

    private var _sampler: Shader? = null
    var sampler: Shader?
        get() = _sampler
        set(value) {
            if (value == null ||_sampler == value) return
            _sampler = value
            shader.setInputShader("gradient", value)
        }

    init {

        shader.setFloatUniform("iTime", 0f)
        shader.setFloatUniform("iCenter", center.x, center.y)
        shader.setFloatUniform("iTimeScale", timeScale)
        shader.setFloatUniform("iSpiralThreshold", spiralThreshold)
        shader.setFloatUniform("iDirection", direction.toFloat())
    }

    companion object {
        private const val SPIRAL_GRADIENT_SHADER = """
    uniform shader gradient;
    uniform float2 iResolution;
    uniform float iTime;
    uniform float iTimeScale;       // make it slower(smaller value) or faster.
    uniform float iDirection;       // -1 out, 1 in.
    uniform float iSpiralThreshold; // bigger more spiral-ish
    uniform float2 iCenter;         // 0.5f by default

    // Remaps a value from one range to another.
    float remap(float v0, float v1, float r0, float r1, float v) {
        return r0 + (r1 - r0) * (v - v0) / (v1 - v0);
    }

    // Converts UV coordinates to spiral coordinates.
    float2 spiral(float2 uv, float spiral, float time) {
        float pi = 3.1415926;
        uv = (2. * uv - 1.) - (2. * iCenter - 1.);

        // Correct for aspect ratio to make the spiral circular.
        if (iResolution.x < iResolution.y) {
            float aspect = iResolution.y / iResolution.x;
            uv.y *= aspect;
        } else {
            float aspect = iResolution.x / iResolution.y;
            uv.x *= aspect;
        }

        float radius = length(uv);
        float angle = atan(uv.y, uv.x);

        float t = remap(-pi, pi, -1., 1., angle);
        t = (t + spiral * radius + time);

        // Return a 2D coordinate where only the x-value changes, suitable for a 1D gradient.
        return float2(t, 0.);
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / iResolution;
        uv = spiral(uv, iSpiralThreshold, iTime*iTimeScale*iDirection);
        return gradient.eval(uv);
    }
"""
    }
}

/**
 * A ShaderBrush that creates an animated spiral gradient.
 *
 * This brush uses a RuntimeShader to transform UV coordinates into a spiral,
 * then applies a linear gradient to those coordinates.
 *
 * @param colors The list of colors to be used in the gradient.
 * @param direction The direction of the spiral animation (in or out).
 * @param spiralThreshold Controls the tightness of the spiral. Higher values create more turns.
 * @param time The current animation time, which drives the spiral's movement.
 * @param timeScale A factor to control the speed of the animation.
 * @param center The center point of the spiral, in relative coordinates (0.5, 0.5 is the center).
 * @param tileMode The tile mode for the underlying linear gradient.
 */
class SpiralGradientBrush(
    sampler: Shader,
    direction: SpiralGradientDirection,
    spiralThreshold: Float,
    timeScale: Float,
    center: Offset,
) : ShaderBrush() {

    private var internalSize =Size.Zero
    val shader = SpiralShader()

    init {
        shader.direction = direction
        shader.spiralThreshold = spiralThreshold
        shader.timeScale = timeScale
        shader.center = center
        shader.sampler = sampler
    }

    override fun createShader(size: Size): Shader {
        internalSize = size
        shader.resolution = size
        return shader.shader
    }

    fun setTime(time: Float) {
        shader.time = time
    }

    fun setSampler(sampler: Shader) {
        shader.sampler = sampler
    }

    fun setCenter(center: Offset) {
        shader.center = center
    }

    fun setDirection(direction: SpiralGradientDirection) {
        shader.direction = direction
    }

    fun setTimeScale(timeScale: Float) {
        shader.timeScale = timeScale
    }

    fun setSpiralThreshold(spiralThreshold: Float) {
        shader.spiralThreshold = spiralThreshold
    }


    companion object {

        fun createSampler(
            colors: GradientColors,
            tileMode: Shader.TileMode,
        ) = LinearGradient(
            0f, 0f, 1f, 0f, // A 1D gradient is sufficient
            colors.colors.map { it.toArgb() }.toIntArray(),
            null,
            tileMode)
    }
}