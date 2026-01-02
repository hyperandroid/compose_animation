package com.spellington.animationtest.gradient.brush

import android.graphics.LinearGradient
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import com.spellington.animationtest.gradient.WavyGradientEffect

enum class WavyGradientOrientation {

    Horizontal, Vertical;

    fun toFloat() = if (this == Horizontal)
        1f
    else
        -1f

}

class WavyGradientShader() {

    val shader = RuntimeShader(WAVY_GRADIENT_SHADER)


    private var _resolution: Size = Size.Zero
    var resolution: Size
        get() = _resolution
        set(value) {
            if (_resolution == value) return
            _resolution = value
            shader.setFloatUniform("iResolution", _resolution.width, _resolution.height)
        }

    private var _direction = WavyGradientOrientation.Horizontal
    var  direction: WavyGradientOrientation
        get() = _direction
        set(value) {

            if (_direction == value) return
            _direction = value
            shader.setFloatUniform("iDirection", _direction.toFloat())
        }

    private var _timeScale = .1f
    var timeScale: Float
        get() = _timeScale
        set(value) {
            if (_timeScale == value) return
            _timeScale = value
            shader.setFloatUniform("iTimeScale", _timeScale)
        }


    private var _time: Float = 0f
    var time: Float
        get() = _time
        set(value) {
            if (_time == value) return
            _time = value
            shader.setFloatUniform("iTime", _time)
        }

    private var _amplitude: Float = 1f
    var amplitude: Float
        get() = _amplitude
        set(value) {
            if (_amplitude == value) return
            _amplitude = value
            shader.setFloatUniform("iAmplitude", _amplitude)
        }

    private var _period: Float = 2f
    var period: Float
        get() = _period
        set(value) {
            if (_period == value) return
            _period = value
            shader.setFloatUniform("iPeriod", _period)
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
        shader.setFloatUniform("iDirection", direction.toFloat())
        shader.setFloatUniform("iTime", 0f)
        shader.setFloatUniform("iTimeScale", timeScale)
        shader.setFloatUniform("iAmplitude", amplitude)
        shader.setFloatUniform("iPeriod", period)
    }

    companion object {
        private const val WAVY_GRADIENT_SHADER = """
                    
            uniform shader gradient;
            uniform float2 iResolution;
            uniform float iTime;
            uniform float iDirection;
            uniform float iTimeScale;       // make it slower(smaller value) or faster.
            uniform float iAmplitude;
            uniform float iPeriod;
        
            float2 horizontalWaves(float2 uv, float amplitude, float period, float time) {
                uv.x += amplitude*sin(uv.y*period + time);   // horizontal waves
                return uv;
            }
                
            float2 verticalWaves(float2 uv, float amplitude, float period, float time) {
                uv.y += amplitude*sin(uv.x*period + time);   // horizontal waves
                return uv;
            }
            
            half4 main(float2 fragCoord) {
                float2 uv = fragCoord.xy/iResolution.xy;
                if (iDirection > 0) {
                    uv = horizontalWaves(uv, iAmplitude, iPeriod, iTime * iTimeScale);
                } else {
                    uv = verticalWaves(uv, iAmplitude, iPeriod, iTime * iTimeScale);
                }
                half4 color = gradient.eval(uv);
                return color;
            }
        """

    }
}

class WavyGradientSampler(
    val sampler: LinearGradient,
    val orientation: WavyGradientOrientation,
)

class WavyGradientBrush(
    sampler: WavyGradientSampler,
    timeScale: Float = .1f,
    amplitude: Float = .1f,
    period: Float = 2f,
) : ShaderBrush() {

    private val shader = WavyGradientShader()
    private var internalSize: Size = Size.Zero

    init {
        setSampler(sampler)
        setTimeScale(timeScale)
        setAmplitude(amplitude)
        setPeriod(period)
    }

    override fun createShader(size: Size): Shader {
        internalSize = size
        shader.resolution = size
        return shader.shader
    }

    fun setAmplitude(amplitude: Float) {
        shader.amplitude = amplitude
    }

    fun setPeriod(period: Float) {
        shader.period = period
    }

    fun setTime(time: Float) {
        shader.time = time
    }

    fun setSampler(sampler: WavyGradientSampler) {
        shader.sampler = sampler.sampler
        shader.direction = sampler.orientation
    }

    fun setTimeScale(timeScale: Float) {
        shader.timeScale = timeScale
    }

    companion object {

        fun createSampler(
            orientation: WavyGradientOrientation,
            colors: GradientColors,
            tileMode: Shader.TileMode
        ) = if (orientation == WavyGradientOrientation.Horizontal) {
            WavyGradientSampler(
                orientation = WavyGradientOrientation.Horizontal,
                sampler = createHorizontalSampler(colors, tileMode)
            )
        }
        else {
            WavyGradientSampler(
                orientation = WavyGradientOrientation.Vertical,
                sampler = createVerticalSampler(colors, tileMode)
            )
        }

        private fun createHorizontalSampler(colors: GradientColors, tileMode: Shader.TileMode) =
            createSampler(0f, 0f, 1f, 0f, colors, tileMode)

        private fun createVerticalSampler(colors: GradientColors, tileMode: Shader.TileMode) =
            createSampler(0f, 0f, 0f, 1f, colors, tileMode)

        private fun createSampler(
            x0: Float,
            y0: Float,
            x1: Float,
            y1: Float,
            colors: GradientColors,
            tileMode: Shader.TileMode,
        ) = LinearGradient(
            x0, y0, x1, y1,
            colors.colors.map { it.toArgb() }.toIntArray(),
            null,
            tileMode
        )
    }
}